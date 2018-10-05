package br.com.joaoapps.faciplac.carona.view.componentes.dialogSelectorItemMap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joaov.caronasolidaria.R;
import com.squareup.picasso.Picasso;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;

public class DialogSelectorItemMapView extends LinearLayout {

    private Context context;
    private View view;
    private LinearLayout llBody;
    private TextView tvName;
    private TextView tvAddress;
    private ImageView imgIcon;
    private OnSelectCaronaUsuario onSelectCaronaUsuario;
    private boolean isShowing;
    private CaronaUsuario caronaUsuario;

    public DialogSelectorItemMapView(Context context) {
        super(context);
        init(context);
    }

    public DialogSelectorItemMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogSelectorItemMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    public void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.item_dialog_map_view, this, true);
        setViews();
        hide();
    }

    public void refreshEstablishment(CaronaUsuario caronaUsuario) {
        if (onSelectCaronaUsuario != null) {
            refreshEstablishment(caronaUsuario, onSelectCaronaUsuario);
        }
    }

    public void refreshEstablishment(final CaronaUsuario caronaUsuario, final OnSelectCaronaUsuario onSelectCaronaUsuario) {

        tvName.setText(caronaUsuario.getNome());
        tvAddress.setText(caronaUsuario.getCelular());
        if (caronaUsuario.getUrlFoto() != null && !caronaUsuario.getUrlFoto().isEmpty()) {
            Picasso.with(context)
                    .load(caronaUsuario.getUrlFoto())
                    .placeholder(R.drawable.icon_user_default)
                    .error(R.drawable.icon_user_default)
                    .into(imgIcon);
        }

        llBody.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectCaronaUsuario.select(caronaUsuario);
            }
        });
        show();
    }

    private void setViews() {
        llBody = view.findViewById(R.id.ll_body);
        tvName = view.findViewById(R.id.tv_name);
        tvAddress = view.findViewById(R.id.tv_address);
        imgIcon = view.findViewById(R.id.img_icon);
    }

    public interface OnSelectCaronaUsuario {
        void select(CaronaUsuario caronaUsuario);
    }

    public void show() {
        if (isShowing) {
            hide();
        }
        float TRANSITION_MOVE = 0;
        long TRANSITION_DURATION = 230;
        llBody.animate().translationY(TRANSITION_MOVE).setDuration(TRANSITION_DURATION);
        isShowing = true;
    }

    public void hide() {
        float TRANSITION_MOVE = 350;
        long TRANSITION_DURATION = 230;
        llBody.animate().translationY(TRANSITION_MOVE).setDuration(TRANSITION_DURATION);
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
    }
}
