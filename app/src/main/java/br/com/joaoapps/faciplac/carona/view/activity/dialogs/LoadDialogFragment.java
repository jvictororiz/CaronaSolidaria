package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joaov.faciplac.caronasolidaria.R;

import java.util.Objects;

import br.com.joaoapps.faciplac.carona.view.activity.SuperActivity;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;

public class LoadDialogFragment extends AppCompatDialogFragment {

    private OnTimeoutListener listenre;

    public static LoadDialogFragment newInstance() {

        Bundle args = new Bundle();

        LoadDialogFragment fragment = new LoadDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_load, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            Drawable d = new ColorDrawable(Color.BLACK);
            d.setAlpha(80);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
            getDialog().getWindow().setBackgroundDrawable(d);
            getDialog().show();

            final Handler handler = new Handler();
            int TIMEOUT = 9000;
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //dismiss();
                    if (getContext() != null) {
//                        AlertUtils.showAlert("Tempo limite excedido, tente novamente mais tarde", Objects.requireNonNull(getActivity()));
                        if (listenre != null) {
                            listenre.timeout();
                        }
                    }

                }
            }, TIMEOUT);


        }

    }

    public void setOnTimeoutListener(OnTimeoutListener listener) {
        this.listenre = listener;
    }

    public interface OnTimeoutListener {
        void timeout();
    }
}