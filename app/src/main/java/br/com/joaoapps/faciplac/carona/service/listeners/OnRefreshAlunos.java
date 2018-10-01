package br.com.joaoapps.faciplac.carona.service.listeners;

import java.util.List;

import br.com.joaoapps.faciplac.carona.model.Usuario;

/**
 * Created by joaov on 07/11/2017.
 */

public interface OnRefreshAlunos {

   public void  getAll(List<Usuario> usuarios);
   public void error(int code);
}
