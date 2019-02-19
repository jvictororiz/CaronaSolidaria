package br.com.joaoapps.faciplac.carona.service.listeners;

/**
 * Created by joaov on 09/11/2017.
 */

public interface OnEventListener<T> {
    void success(T object);
    void error(int code);
}
