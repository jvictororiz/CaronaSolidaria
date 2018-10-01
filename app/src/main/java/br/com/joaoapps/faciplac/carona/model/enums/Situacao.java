package br.com.joaoapps.faciplac.carona.model.enums;

/**
 * Created by joaov on 15/11/2017.
 */

public enum Situacao {
    APROVADO,
    NEGADO,
    ESPERA;

    @Override
    public String toString() {
        return name();
    }
}
