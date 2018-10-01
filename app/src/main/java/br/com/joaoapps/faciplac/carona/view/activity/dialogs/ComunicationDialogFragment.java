package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joaov.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.squareup.picasso.Picasso;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;

public class ComunicationDialogFragment extends AppCompatDialogFragment {
    private final int TIME_TO_RESPONSE = 30000;
    private View view;
    private TextView tvName, tvMessage, tvTime;
    private ImageView imgPositive, imgNegative;
    private CircleImageView imgProfile;

    private CaronaUsuario myUser, otherUser;
    private StatusCarona statusMyUser;

    public static ComunicationDialogFragment newInstance(StatusCarona statusMyUser, CaronaUsuario myUser, CaronaUsuario otherUser) {
        Bundle args = new Bundle();
        args.putSerializable("MY_USER", myUser);
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
            myUser = (CaronaUsuario) getActivity().getIntent().getSerializableExtra("MY_USER");
            otherUser = (CaronaUsuario) getActivity().getIntent().getSerializableExtra("OTHER_USER");
            statusMyUser = (StatusCarona) getActivity().getIntent().getSerializableExtra("STATUS_MY_USER");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_comunication, container, false);
        setViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTimeToDeny();
        setEventsClicks();
        configOtherUserInView();
        configMyUserInView();


    }

    private void configMyUserInView() {
        switch (statusMyUser) {
            case DAR_CARONA:
                tvMessage.setText("Olá" + myUser.getNome() + ", Você poderia me da uma carona?");
                break;
            case RECEBER_CARONA:
                tvMessage.setText("Olá" + myUser.getNome() + ", Você poderia me da uma carona?");
                break;
        }
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
            }
        });

        imgNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                denyOption();
            }
        });
    }

    private void initTimeToDeny() {
        new CountDownTimer(TIME_TO_RESPONSE, 1000) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > 1000) {
                    tvTime.setText((String.valueOf(millisUntilFinished / 1000)).concat(" Segundos"));
                } else {
                    tvTime.setText((String.valueOf(millisUntilFinished / 1000)).concat(" Segundo"));
                }
            }

            public void onFinish() {
                denyOption();
            }

        }.start();
    }

    private void denyOption() {

    }

    private void acceptOption() {

    }

    private void setViews() {
        tvMessage = view.findViewById(R.id.tv_message);
        tvName = view.findViewById(R.id.tv_name);
        tvTime = view.findViewById(R.id.tv_time);
        imgNegative = view.findViewById(R.id.img_negative);
        imgPositive = view.findViewById(R.id.img_positive);
        imgProfile = view.findViewById(R.id.img_profile);
    }
}