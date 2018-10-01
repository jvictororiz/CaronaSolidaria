package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.view.activity.SuperActivity;

public class LoadDialogFragment extends AppCompatDialogFragment {
    private final int TIMEOUT = 150000;

    public static LoadDialogFragment newInstance() {

        Bundle args = new Bundle();

        LoadDialogFragment fragment = new LoadDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_load, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            Drawable d = new ColorDrawable(Color.BLACK);
            d.setAlpha(80);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setLayout(width, height);
            getDialog().getWindow().setBackgroundDrawable(d);
            getDialog().show();

            final Handler handler = new Handler();
            handler.postDelayed( new Runnable() {

                @Override
                public void run() {
                    dismiss();
                    if(getContext() != null) {
                        SuperActivity.startActivityMessageNegative(getContext(), null, "Falha na conexão");
                    }

                }
            }, TIMEOUT );


        }
    }
}