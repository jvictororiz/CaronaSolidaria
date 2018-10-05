package br.com.joaoapps.faciplac.carona.view.componentes.dialogSelectorItemMap;

import android.animation.ValueAnimator;
import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joaov.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.squareup.picasso.Picasso;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.utils.AnimationUtils;
import br.com.joaoapps.faciplac.carona.view.utils.AppUtil;
import br.com.joaoapps.faciplac.carona.view.utils.GpsUtils;
import br.com.joaoapps.faciplac.carona.view.utils.Mask;

public class DialogItemMapView extends LinearLayout {

    private final float DURATION_HIDE_OR_SHOW = 400;

    private Context context;
    private View view;
    private TextView tvDistance, tvName, tvNameBig, tvNumber, tvAdress, tvaAdressBig;
    private View viewCall;
    private View viewRoute;
    private View llBody;
    private ImageView imgIcon;
    private OnEventesCaronaUsuarioDetailed onEventesCaronaUsuarioDetailed;
    private View llBodyCollected;
    private View llBodyExpanded;
    private View llBodyExpandedTop;
    private ViewGroup llBodyDistance;
    private View llBodyCollectedTop;
    private CaronaUsuario caronaUsuario;
    private TYPE_STATE currentState;
    private CircleImageView imgProfile;
    private TextView tvButtonBig;

    public DialogItemMapView(Context context) {
        super(context);
        init(context);
    }

    public DialogItemMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogItemMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    public void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.item_dialog_map_view_completed, this, true);
        setViews();
        currentState = TYPE_STATE.HIDE;
        hideNoAnimation();
    }

    public void refreshCaronaUsuarios(Location locationActual, final CaronaUsuario caronaUsuario, final OnEventesCaronaUsuarioDetailed onEventesCaronaUsuarioDetailed) {
        this.caronaUsuario = caronaUsuario;
        this.onEventesCaronaUsuarioDetailed = onEventesCaronaUsuarioDetailed;
        viewRoute.setVisibility(VISIBLE);

        if (locationActual != null) {
            tvDistance.setText("A ".concat(GpsUtils.distanceTo(locationActual, getLocationEstablishmenbt(caronaUsuario)).concat(" de distância de você")));
        }

        tvName.setText(caronaUsuario.getNome());
        tvNameBig.setText(caronaUsuario.getNome());
        String nameLocale = GpsUtils.getNameLocale(context, caronaUsuario.getPositionResidence().getLatitude(), caronaUsuario.getPositionResidence().getLongitude());
        tvAdress.setText(nameLocale);
        tvaAdressBig.setText(nameLocale);
        tvNumber.setText(Mask.addMask(caronaUsuario.getCelular(), "######****-**##"));
        tvButtonBig.setText(caronaUsuario.getStatusCarona() == StatusCarona.DAR_CARONA ? "Pedir carona" : "Oferecer carona");
        if (caronaUsuario.getUrlFoto() != null && !caronaUsuario.getUrlFoto().isEmpty()) {
            Picasso.with(context)
                    .load(caronaUsuario.getUrlFoto())
                    .placeholder(R.drawable.icon_carona)
                    .error(R.drawable.icon_user_default)
                    .into(imgProfile);
        }
    }

    private Location getLocationEstablishmenbt(CaronaUsuario caronaUsuario) {
        Location location = new Location("");
        location.setLatitude((caronaUsuario.getLatitude()));
        location.setLongitude((caronaUsuario.getLongitude()));
        return location;
    }

    public void showExpanded() {
        if (currentState == TYPE_STATE.HIDE) {
            showCollected();
        }
        currentState = TYPE_STATE.EXPANDED;
        setVisibleAnimate(llBodyExpanded);
        setVisibleAnimate(llBodyExpandedTop);
        setVisibleAnimate(imgProfile);
        setVisibleAnimate(llBodyDistance);
        llBodyCollected.setVisibility(INVISIBLE);
        AnimationUtils.setVisible(this, llBodyExpandedTop);
        setInvisibleAnimate(llBodyCollected);
        setInvisibleAnimate(llBodyCollectedTop);
        viewRoute.animate().setDuration(300).translationY(AppUtil.dpToPx(0));
        setEventsClick();
    }

    public void showCollected() {
        currentState = TYPE_STATE.COLLECTED;
        llBody.animate().translationY(0).setDuration(600);
        setVisibleAnimate(DialogItemMapView.this);
        setVisibleAnimate(llBodyCollected);
        setInvisibleAnimate(llBodyDistance);
        setVisibleAnimate(llBodyCollectedTop);
        setInvisibleAnimate(llBodyExpanded);
        setInvisibleAnimate(imgProfile);
        setInvisibleAnimate(llBodyExpandedTop);
        viewRoute.animate().setDuration(180).translationY(AppUtil.dpToPx(-45));

        llBodyCollected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventesCaronaUsuarioDetailed.onClickCollected();
            }
        });
    }

    public void hide() {
        currentState = TYPE_STATE.HIDE;
        llBody.animate().translationY(3000).setDuration(600);
    }

    private void hideNoAnimation() {
        currentState = TYPE_STATE.HIDE;
        llBody.animate().translationY(3000).setDuration(0);
    }

    private void setEventsClick() {
        viewCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventesCaronaUsuarioDetailed.onCall(caronaUsuario.getCelular());
            }
        });

        llBodyExpanded.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onEventesCaronaUsuarioDetailed.onClickComunication(caronaUsuario);
            }
        });
    }

    private void setViews() {
        tvDistance = view.findViewById(R.id.tv_distance);
        tvNumber = view.findViewById(R.id.tv_number);
        tvAdress = view.findViewById(R.id.tv_address);
        tvaAdressBig = view.findViewById(R.id.tv_address_big);
        tvButtonBig = view.findViewById(R.id.tv_button_big);
        tvName = view.findViewById(R.id.tv_name);
        tvNameBig = view.findViewById(R.id.tv_name_big);
        viewCall = view.findViewById(R.id.view_call);
        imgProfile = view.findViewById(R.id.img_profile);
        viewRoute = view.findViewById(R.id.route);
        llBody = view.findViewById(R.id.ll_body);
        llBodyCollected = view.findViewById(R.id.ll_body_collected);
        llBodyExpanded = view.findViewById(R.id.ll_body_expanded);
        llBodyExpandedTop = view.findViewById(R.id.ll_body_top_expanded);
        llBodyDistance = view.findViewById(R.id.ll_body_distance);
        llBodyCollectedTop = view.findViewById(R.id.ll_body_collected_top);
        imgIcon = view.findViewById(R.id.img_icon);
    }

    private void setInvisibleAnimate(final View view) {
        view.animate().setDuration(600).alpha(0f).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setVisibility(GONE);
            }
        });
    }

    private void setVisibleAnimate(final View view) {
        view.animate().setDuration(600).alpha(1f).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setVisibility(VISIBLE);
            }
        });
    }

    public void unmaskNumber() {
        tvNumber.setText(caronaUsuario.getCelular());
    }

    public void hideButton() {
        viewRoute.setVisibility(GONE);
    }

    public enum TYPE_STATE {
        EXPANDED,
        COLLECTED,
        HIDE
    }

    public TYPE_STATE getCurrentState() {
        return currentState;
    }

    public interface OnEventesCaronaUsuarioDetailed {
        void onClickCollected();

        void onCall(String number);

        void onClickComunication(CaronaUsuario caronaUsuario);
    }
}
