package br.com.joaoapps.faciplac.carona.service.firebase.push.objects;

/**
 * Created by joaov on 19/11/2017.
 */

public class Notification {

    private String title;
    private String body;
    private String icon;
    private String sound;
    private String pushIdRemetente;

    public Notification(String title, String text) {
        this.title = title;
        this.body = text;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPushIdRemetente() {
        return pushIdRemetente;
    }

    public void setPushIdRemetente(String pushIdRemetente) {
        this.pushIdRemetente = pushIdRemetente;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Notification() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return body;
    }

    public void setText(String text) {
        this.body = text;
    }
}
