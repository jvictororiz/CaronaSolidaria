package br.com.joaoapps.faciplac.carona.view.activity.dialogs.listeners;

import java.util.List;

import br.com.joaoapps.faciplac.carona.model.enums.Situacao;

/**
 * Created by joaov on 15/11/2017.
 */

public interface OnEventDialogListener {

    void onDismiss(List<Situacao> situacoes) ;
    void onOpen();
    void onChange();
}
