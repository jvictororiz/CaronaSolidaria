package br.com.joaoapps.faciplac.carona.service.listeners;

/**
 * Created by joaov on 09/11/2017.
 */

public interface OnTransacaoListener {
    void success(Object object);
    void error(int code);
}
