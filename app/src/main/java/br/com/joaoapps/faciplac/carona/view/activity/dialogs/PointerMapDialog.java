package br.com.joaoapps.faciplac.carona.view.activity.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;

import com.example.joaov.caronasolidaria.R;

import java.util.ArrayList;
import java.util.List;

import br.com.joaoapps.faciplac.carona.model.enums.Situacao;
import br.com.joaoapps.faciplac.carona.view.activity.dialogs.listeners.OnEventDialogListener;

/**
 * Created by joaov on 15/11/2017.
 */

public class PointerMapDialog extends BottomSheetDialog {
    private OnEventDialogListener onEventDialogListener;
    private List<Situacao> situacoes;

    AppCompatCheckBox ckTodos;
    AppCompatCheckBox ckAprovados;
    AppCompatCheckBox ckNegados;
    AppCompatCheckBox ckEspera;

    public PointerMapDialog(Context context, OnEventDialogListener onEventDialogListener) {
        super(context, R.style.CoffeeDialog);

        situacoes  = new ArrayList<>();
        this.onEventDialogListener = onEventDialogListener;
        View contentView = View.inflate(getContext(), R.layout.dialog_bottom, null);
        setContentView(contentView);

        ckAprovados = (AppCompatCheckBox) findViewById(R.id.ck_aprovados);
        ckEspera = (AppCompatCheckBox) findViewById(R.id.ck_espera);
        ckNegados = (AppCompatCheckBox) findViewById(R.id.ck_negados);
        ckTodos = (AppCompatCheckBox) findViewById(R.id.ck_todos);

        configureBottomSheetBehavior(contentView);

        ckAprovados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               verificarTodos();
            }
        });

        ckEspera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarTodos();
            }
        });

        ckNegados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarTodos();
            }
        });

        ckTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ckTodos.isChecked()) {
                    ckAprovados.setChecked(true);
                    ckNegados.setChecked(true);
                    ckEspera.setChecked(true);
                }else{
                    ckAprovados.setChecked(false);
                    ckNegados.setChecked(false);
                    ckEspera.setChecked(false);
                }

            }
        });

    }

    private void verificarTodos() {
        if(ckEspera.isChecked() && ckNegados.isChecked() && ckAprovados.isChecked()){
            ckTodos.setChecked(true);
        }else{
            ckTodos.setChecked(false);
        }
    }

    private void configureBottomSheetBehavior(View contentView) {
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBottomSheetBehavior.setHideable(true);
        setCanceledOnTouchOutside(true);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                montarLista();
                onEventDialogListener.onDismiss(situacoes);
            }
        });

        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            dismiss();
                            montarLista();
                            onEventDialogListener.onDismiss(situacoes);
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            onEventDialogListener.onOpen();
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    Log.i("log", slideOffset+"");
                }
            });
        }
    }

    private void montarLista() {
        situacoes = new ArrayList<>();

        if(ckEspera.isChecked()){
            situacoes.add(Situacao.ESPERA);
        }
        if(ckNegados.isChecked()){
            situacoes.add(Situacao.NEGADO);
        }
        if(ckAprovados.isChecked()){
            situacoes.add(Situacao.APROVADO);
        }
    }
}
