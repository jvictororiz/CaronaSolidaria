package br.com.joaoapps.faciplac.carona.view.activity.maps.mapConfiguration;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomRender extends DefaultClusterRenderer<MarkerItem> {
    private final int MAX_ITENS_TO_JOIN = 1;
    private Context context;

    public CustomRender(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {
        Bitmap bitmap = new IconMarkerGenerator(context).makeIconSmall(item.getUrlIcon(), item.isBig());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MarkerItem> cluster, MarkerOptions markerOptions) {
        Bitmap bitmap = new IconMarkerGenerator(context).makeIconBig(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title(String.valueOf(cluster.getSize()));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > MAX_ITENS_TO_JOIN;
    }

}