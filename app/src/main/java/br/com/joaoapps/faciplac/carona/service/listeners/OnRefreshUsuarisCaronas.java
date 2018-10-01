package br.com.joaoapps.faciplac.carona.service.listeners;

import java.util.List;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.firebase.UsuarioFirebase;

/**
 * Created by joaov on 20/11/2017.
 */

public interface OnRefreshUsuarisCaronas {
    void onSuccess(List<CaronaUsuario> usuarioFirebase);
    void onError(int code);
}
