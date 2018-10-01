package br.com.joaoapps.faciplac.carona.service.listeners;

import br.com.joaoapps.faciplac.carona.model.Usuario;

/**
 * Created by joaov on 09/11/2017.
 */

public interface OnResetSenha {
    void success(Usuario usuario);
    void error(int code);
}
