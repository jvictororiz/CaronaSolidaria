package br.com.joaoapps.faciplac.carona.view.componentes.chat.objects;

import com.stfalcon.chatkit.commons.models.IUser;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.Usuario;

public class UsuarioMessage implements IUser {
    private CaronaUsuario usuario;

    public UsuarioMessage(CaronaUsuario usuario) {
        this.usuario = usuario;
    }

    public CaronaUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(CaronaUsuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String getId() {
        return usuario.getPushId();
    }

    @Override
    public String getName() {
        return usuario.getNome();
    }

    @Override
    public String getAvatar() {
        return usuario.getUrlFoto();
    }
}
