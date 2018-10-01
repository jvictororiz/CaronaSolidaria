package br.com.joaoapps.faciplac.carona.view.componentes.search;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.componentes.sheetCardSelector.SelectorDialogBottom;


public class SearchViewHV extends LinearLayout {
    private Context context;
    private View view;
    private View llBody;
    private SearchView search;
    private View llBodyChangeCarona;
    private ImageView imgSelector;
    private SelectorDialogBottom selectorDialogBottom;
    private StatusCarona statusCaronaActual;
    private boolean isFirst = true;
    private ViewGroup llRefresh;
    private ImageView imgRefresh;

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
    }

    public void setRefreshListener(final OnClickListener onClickListener) {
        llRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    imgRefresh.animate().setDuration(500).rotation(imgRefresh.getRotation() + 180);
                onClickListener.onClick(view);
            }
        });
    }

    public void configEventChangeType(StatusCarona statusCarona, final SelectorDialogBottom.OnStatusCarona onStatusCarona, final OnQurySubmit onQurySubmit) {
        search.onActionViewExpanded();
        statusCaronaActual = statusCarona;
        selectorDialogBottom = new SelectorDialogBottom(context, statusCaronaActual, new SelectorDialogBottom.OnStatusCarona() {
            @Override
            public void selected(StatusCarona statusCarona) {
                statusCaronaActual = statusCaronaActual.equals(StatusCarona.RECEBER_CARONA) ? StatusCarona.DAR_CARONA : StatusCarona.RECEBER_CARONA;
                changeStatusView(statusCaronaActual);
                onStatusCarona.selected(statusCaronaActual);
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

    }

    private void changeStatusView(StatusCarona statusCarona) {
        if (statusCarona.equals(StatusCarona.DAR_CARONA)) {
            imgSelector.setImageDrawable(getResources().getDrawable(R.drawable.icon_carona));
        } else if (statusCarona.equals(StatusCarona.RECEBER_CARONA)) {
            imgSelector.setImageDrawable(getResources().getDrawable(R.drawable.icon_pedindo_carona));
        }
    }

    public void show() {
        llBody.animate().setDuration(580).translationY(0);
    }

    public StatusCarona getStatusCaronaActual() {
        return statusCaronaActual;
    }

    public void hide() {
        llBody.animate().setDuration(580).translationY(-280);
    }

    public interface OnQurySubmit {
        void submit(String query);
    }


}
