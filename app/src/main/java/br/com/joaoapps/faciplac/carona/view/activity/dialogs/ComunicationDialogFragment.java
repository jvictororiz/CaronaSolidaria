package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.ComunicationCaronaBody;

public class ComunicationDialogFragment extends AppCompatDialogFragment {
    private final int TIME_TO_RESPONSE = 59000;
    private View view;
    private TextView tvName, tvMessage, tvTime;
    private ImageView imgPositive, imgNegative;
    private CircleImageView imgProfile;

    private CaronaUsuario myUser, otherUser;
    private Button btnSeeProfile;
    private ViewGroup bodyStepOne;
    private StatusCarona statusMyUser;
    private OnClickButtons onClickButons;
    private String message;
    private int step;
    private View.OnClickListener onClickButtonSeeProfile;

    public static ComunicationDialogFragment newInstance(String message, StatusCarona statusMyUser, CaronaUsuario myUser, CaronaUsuario otherUser) {
        Bundle args = new Bundle();
        args.putSerializable("MY_USER", myUser);
        args.putSerializable("MESSAGE", message);
        args.putSerializable("OTHER_USER", otherUser);
        args.putSerializable("STATUS_MY_USER", statusMyUser);
        ComunicationDialogFragment fragment = new ComunicationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            assert getArguments() != null;
            myUser = (CaronaUsuario) getArguments().getSerializable("MY_USER");
            otherUser = (CaronaUsuario) getArguments().getSerializable("OTHER_USER");
            statusMyUser = (StatusCarona) getArguments().getSerializable("STATUS_MY_USER");
            message = getArguments().getString("MESSAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_comunication, container, false);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEventsClicks();
        configViews();
        configOtherUserInView();
        configMyUserInView();


    }

    private void configViews() {
        switch (step) {
            case ComunicationCaronaBody.STEP_ONE_COMUNICATION:
                btnSeeProfile.setVisibility(View.GONE);
                bodyStepOne.setVisibility(View.VISIBLE);
                break;
            case ComunicationCaronaBody.STEP_TWO_ACCEPT:
                btnSeeProfile.setVisibility(View.VISIBLE);
                bodyStepOne.setVisibility(View.GONE);
                btnSeeProfile.setOnClickListener(onClickButtonSeeProfile);
                break;
            case ComunicationCaronaBody.STEP_TWO_DENIED:
                btnSeeProfile.setText(R.string.fechar);
                btnSeeProfile.setVisibility(View.VISIBLE);
                bodyStepOne.setVisibility(View.GONE);
                btnSeeProfile.setOnClickListener(view -> dismiss());
                break;
        }
    }

    private void configMyUserInView() {
        tvMessage.setText(message);
//        if (myUser != null) {
//            switch (statusMyUser) {
//                case RECEBER_CARONA:
//                    tvMessage.setText("Olá " + (myUser.getNome().concat(", você aceita uma carona?")));
//                    break;
//                case DAR_CARONA:
//                    tvMessage.setText("Olá " + (myUser.getNome().concat(", você poderia me da uma carona?")));
//                    break;
//            }
//        }
    }

    private void configOtherUserInView() {
        tvName.setText(otherUser.getNome());
        if (otherUser != null && otherUser.getUrlFoto() != null && !otherUser.getUrlFoto().isEmpty()) {
            Picasso.with(getActivity())
                    .load(otherUser.getUrlFoto())
                    .placeholder(R.drawable.icon_carona)
                    .error(R.drawable.icon_user_default)
                    .into(imgProfile);
        }


    }

    private void setEventsClicks() {
        imgPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOption();
                if (onClickButons != null) {
                    onClickButons.positive();
                }
            }
        });

        imgNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                denyOption();
                if (onClickButons != null) {
                    onClickButons.negative();
                }
            }
        });
    }



    public void initTimeToDeny() {
        new CountDownTimer(TIME_TO_RESPONSE, 1000) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > 1000) {
                    tvTime.setText("Tempo para responder:\n"+(String.valueOf(millisUntilFinished / 1000)).concat(" Segundos"));
                } else {
                    tvTime.setText("Tempo para responder:\n"+(String.valueOf(millisUntilFinished / 1000)).concat(" Segundo"));
                }
            }

            public void onFinish() {
                denyOption();
            }

        }.start();
    }

    private void denyOption() {
        dismiss();
    }

    private void acceptOption() {
        dismiss();
    }

    private void setViews() {
        tvMessage = view.findViewById(R.id.tv_message);
        tvName = view.findViewById(R.id.tv_name);
        tvTime = view.findViewById(R.id.tv_time);
        imgNegative = view.findViewById(R.id.img_negative);
        imgPositive = view.findViewById(R.id.img_positive);
        imgProfile = view.findViewById(R.id.img_profile);
        btnSeeProfile = view.findViewById(R.id.btn_see_profile);
        bodyStepOne = view.findViewById(R.id.body_step_one);
    }

    public void configToStepOne(OnClickButtons onClickButtons) {
        this.onClickButons = onClickButtons;
        this.step = ComunicationCaronaBody.STEP_ONE_COMUNICATION;

    }

    public void configToStepTwo(View.OnClickListener onClickListener) {
        this.onClickButtonSeeProfile = onClickListener;
        this.step = ComunicationCaronaBody.STEP_TWO_ACCEPT;
    }

    public void configToStepThree() {
        this.step = ComunicationCaronaBody.STEP_TWO_DENIED;
    }

    public interface OnClickButtons {
        void positive();

        void negative();
    }
}