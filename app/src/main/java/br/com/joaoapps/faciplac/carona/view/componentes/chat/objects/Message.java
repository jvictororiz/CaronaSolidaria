package br.com.joaoapps.faciplac.carona.view.componentes.chat.objects;

import android.support.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class Message implements IMessage, MessageContentType.Image, MessageContentType {
    private String message;
    private String id;
    private IUser user;
    private Date dateMessage;
    private String urlImage;

    public Message(String message, String id, IUser user, Date dateMessage, String urlImage) {
        this.message = message;
        this.id = id;
        this.user = user;
        this.dateMessage = dateMessage;
        this.urlImage = urlImage;
    }

    public void setText(String message) {
        this.message = message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public Date getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(Date dateMessage) {
        this.dateMessage = dateMessage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return message;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return dateMessage;
    }


    @Nullable
    @Override
    public String getImageUrl() {
        return urlImage;
    }
}
