package br.com.joaoapps.faciplac.carona.model.enums;

import java.io.Serializable;

/**
 * Created by joaov on 20/11/2017.
 */

public enum StatusCarona implements Serializable{
    DAR_CARONA,
    RECEBER_CARONA,
    DESATIVADO;

    @Override
    public String toString() {
        return name();
    }
}
