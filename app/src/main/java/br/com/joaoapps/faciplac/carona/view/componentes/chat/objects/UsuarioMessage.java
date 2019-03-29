package br.com.joaoapps.faciplac.carona.view.componentes.chat.objects;

import com.stfalcon.chatkit.commons.models.IUser;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;

public class UsuarioMessage implements IUser {
    private CaronaUsuario user;

    public UsuarioMessage(CaronaUsuario user) {
        this.user = user;
    }

    public CaronaUsuario getUser() {
        return user;
    }

    public void setUser(CaronaUsuario user) {
        this.user = user;
    }

    @Override
    public String getId() {
        return user.getPushId();
    }

    @Override
    public String getName() {
        return user.getNome();
    }

    @Override
    public String getAvatar() {
        return user.getUrlFoto();
    }
}
