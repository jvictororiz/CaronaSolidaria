package br.com.joaoapps.faciplac.carona.view.componentes.sheetDialogEstablishments;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.view.utils.GpsUtils;

public class EstablishmentsAdapter extends RecyclerView.Adapter<EstablishmentsAdapter.EstablishmentsViewHolder> {
    private final CaronaUsuario myCaronaUsuario;
    private List<CaronaUsuario> caronaUsuarios;
    private OnClickPosition onClickPosition;
    private Context context;

    public EstablishmentsAdapter(CaronaUsuario myUsuario, List<CaronaUsuario> caronaUsuarios, Context context, OnClickPosition onClickPosition) {
        this.caronaUsuarios = caronaUsuarios;
        this.myCaronaUsuario = myUsuario;
        this.onClickPosition = onClickPosition;
        this.context = context;
    }

    @NonNull
    @Override
    public EstablishmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carona_usuario, parent, false);
        return new EstablishmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EstablishmentsViewHolder holder, int position) {
        CaronaUsuario caronaUsuario = caronaUsuarios.get(position);
        holder.tvAddress.setText(GpsUtils.getNameLocale(context, caronaUsuario.getPositionResidence().getLatitude(), caronaUsuario.getPositionResidence().getLongitude()));
        holder.tvDistance.setText("A ".concat(GpsUtils.distanceTo(getLocation(myCaronaUsuario), getLocation(caronaUsuario)).concat(" de você")));
        holder.tvName.setText(caronaUsuario.getNome());
        if (caronaUsuario.getUrlFoto() != null && !caronaUsuario.getUrlFoto().isEmpty()) {
            Picasso.with(context)
                    .load(caronaUsuario.getUrlFoto())
                    .placeholder(R.drawable.icon_user_default)
                    .error(R.drawable.icon_user_default)
                    .into(holder.imgIcon);
        }


        holder.llBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPosition.click(holder.getAdapterPosition());
            }
        });

    }

    private Location getLocation(CaronaUsuario myCaronaUsuario) {
        Location myLocation = new Location("");
        if (myCaronaUsuario != null) {
            myLocation.setLatitude(myCaronaUsuario.getLatitude());
            myLocation.setLongitude(myCaronaUsuario.getLongitude());
        }
        return myLocation;
    }

    @Override
    public int getItemCount() {
        return caronaUsuarios.size();
    }

    public class EstablishmentsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvExtra;
        private TextView tvDistance;
        private TextView tvAddress;
        private ImageView imgIcon;
        private ViewGroup llBody;


        public EstablishmentsViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_name);
            tvExtra = v.findViewById(R.id.tv_extra);
            tvDistance = v.findViewById(R.id.tv_distance);
            tvAddress = v.findViewById(R.id.tv_address);
            llBody = v.findViewById(R.id.ll_body);
            imgIcon = v.findViewById(R.id.img_icon);

        }
    }


}
