package br.com.joaoapps.faciplac.carona.model.enums;

/**
 * Created by joaov on 07/11/2017.
 */

public enum Status {
    ALUNO,
    DIRETOR,
    SUB_DIRETOR;

    @Override
    public String toString() {
        return name();
    }
}
