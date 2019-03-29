package br.com.joaoapps.faciplac.carona.view.componentes.search;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.componentes.sheetCardSelector.SelectorDialogBottom;
import br.com.joaoapps.faciplac.carona.view.utils.AnimationUtils;
import br.com.joaoapps.faciplac.carona.view.utils.GpsUtils;


public class SearchViewHV extends LinearLayout {
    private Context context;
    private View view;
    private ViewGroup llBody;
    private SearchView search;
    private View llBodyChangeCarona;
    private ImageView imgSelector;
    private SelectorDialogBottom selectorDialogBottom;
    private StatusCarona statusCaronaActual;
    private boolean isFirst = true;
    private ViewGroup llRefresh, llBodyDistance;
    private SeekBar seekBar;
    private TextView tvDistance;
    private ImageView imgRefresh;

    private OnDistanceListener onDistanceListener;
    private float distanceActual;
    private CheckBox ckHasDistanceMin;

    public SearchViewHV(Context context) {
        super(context);
        init(context);
    }

    public SearchViewHV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchViewHV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchViewHV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.search_view_hv, this, true);
        setViews();
    }

    private void setViews() {
        llBody = view.findViewById(R.id.ll_body);
        llRefresh = view.findViewById(R.id.ll_refresh);
        search = view.findViewById(R.id.search);
        llBodyChangeCarona = view.findViewById(R.id.ll_filter);
        imgSelector = view.findViewById(R.id.fb_type_card);
        imgRefresh = view.findViewById(R.id.fb_refresh);
        llBodyDistance = view.findViewById(R.id.ll_body_distance);
        tvDistance = view.findViewById(R.id.tv_distance);
        seekBar = view.findViewById(R.id.seekbar);
        ckHasDistanceMin = view.findViewById(R.id.check_distance_min);
    }

    public void setRefreshListener(final OnClickListener onClickListener) {
        llRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRefresh.animate().setDuration(700).rotation(imgRefresh.getRotation() + 720);
                onClickListener.onClick(view);
            }
        });
    }

    public void setListenerDistance(final OnDistanceListener onDistanceListener) {
        this.onDistanceListener = onDistanceListener;
        final int step = 1;
        int max = 2000;
        final int min = 200;
        distanceActual = min + (max / 2 * step);
        seekBar.setMax((max - min) / step);
        seekBar.setProgress(max / 2);
        tvDistance.setText("Limitado aos usuários que moram até " + (GpsUtils.getTextToDistance(distanceActual)) + " de você");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                distanceActual = min + (progress * step);
                tvDistance.setText("Limitado aos usuários que moram até " + (GpsUtils.getTextToDistance(distanceActual)) + " de você");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onDistanceListener.changed(seekBar.getProgress());
            }
        });
    }

    public void setListenerCheckbox(CompoundButton.OnCheckedChangeListener listener){
        ckHasDistanceMin.setOnCheckedChangeListener(listener);
    }

    public void configEventChangeType(StatusCarona statusCarona, final SelectorDialogBottom.OnStatusCarona onStatusCarona, final OnQurySubmit onQurySubmit) {
        search.onActionViewExpanded();
        statusCaronaActual = statusCarona;
        changeStatusView(statusCaronaActual);
        selectorDialogBottom = new SelectorDialogBottom(context, statusCaronaActual, new SelectorDialogBottom.OnStatusCarona() {
            @Override
            public void selected(StatusCarona statusCarona) {
                if (statusCarona != statusCaronaActual) {
                    statusCaronaActual = statusCaronaActual.equals(StatusCarona.RECEBER_CARONA) ? StatusCarona.DAR_CARONA : StatusCarona.RECEBER_CARONA;
                    changeStatusView(statusCaronaActual);
                    onStatusCarona.selected(statusCaronaActual);
                }
            }
        });


        llBodyChangeCarona.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirst) {
                    selectorDialogBottom.show(statusCaronaActual);
                } else {
                    selectorDialogBottom.show();
                }
                isFirst = false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQurySubmit.submit(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    onQurySubmit.submit("");
                }
                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                search.clearFocus();
            }
        }, 350);


    }

    private void changeStatusView(StatusCarona statusCarona) {
        if (statusCarona.equals(StatusCarona.DAR_CARONA)) {
            imgSelector.setImageDrawable(getResources().getDrawable(R.drawable.icon_carona));
        } else if (statusCarona.equals(StatusCarona.RECEBER_CARONA)) {
            imgSelector.setImageDrawable(getResources().getDrawable(R.drawable.icon_pedindo_carona));
        }
    }

    private void setInvisibleAnimate(final View view) {
        AnimationUtils.setInvisible(llBody, view);
    }

    private void setVisibleAnimate(final View view) {
        AnimationUtils.setVisible(llBody, view);
    }

    public void show() {
        llBody.animate().setDuration(780).translationY(0);
    }

    public StatusCarona getStatusCaronaActual() {
        return statusCaronaActual;
    }

    public void hide() {
        llBody.animate().setDuration(780).translationY(-380);
    }

    public float getDistanceMin() {
        return distanceActual;
    }

    public void clearFocousSearch() {
        search.clearFocus();
    }

    public void hideMinDistanceViews() {
        setInvisibleAnimate(llBodyDistance);
    }

    public void showMinDistanceViews() {
        setVisibleAnimate(llBodyDistance);
    }

    public interface OnQurySubmit {
        void submit(String query);
    }

    public interface OnDistanceListener {
        void changed(long distance);
    }

    public ImageView getImgRefresh() {
        return imgRefresh;
    }

    public ImageView getImgSelector() {
        return imgSelector;
    }
}
