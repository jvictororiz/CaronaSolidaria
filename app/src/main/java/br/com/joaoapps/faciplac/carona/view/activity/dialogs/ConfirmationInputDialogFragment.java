package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;

import java.io.Serializable;
import java.util.Objects;

public class ConfirmationInputDialogFragment extends AppCompatDialogFragment {
    private final int TIME_TO_RESPONSE = 59000;
    private OnConfirmInput onConfirmInput;
    private View view;
    private EditText edtInput;
    private TextView tvMessage;
    private View imgClose, btnSend;
    private String message;

    public static ConfirmationInputDialogFragment newInstance(String message, OnConfirmInput onConfirmInput) {
        Bundle args = new Bundle();
        args.putSerializable("EVENT", onConfirmInput);
        args.putString("MESSAGE", message);
        ConfirmationInputDialogFragment fragment = new ConfirmationInputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getArguments() != null) {
            onConfirmInput = (OnConfirmInput) getArguments().getSerializable("EVENT");
            message = getArguments().getString("MESSAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_confirmation_dialog, container, false);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configViews();
    }

    private void configViews() {
        tvMessage.setText(message);
        imgClose.setOnClickListener(v -> dismiss());
        btnSend.setOnClickListener(v -> {
            String message = edtInput.getText().toString();
            onConfirmInput.OnSendListener(message);
            dismiss();
        });
    }


    private void setViews() {
        btnSend = view.findViewById(R.id.btn_send);
        imgClose = view.findViewById(R.id.img_close);
        edtInput = view.findViewById(R.id.edt_input);
        tvMessage = view.findViewById(R.id.tv_message);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, "CONFIRMATION_INPUT)_DIALOG");
    }

    public interface OnConfirmInput extends Serializable {
        void OnSendListener(String input);
    }
}