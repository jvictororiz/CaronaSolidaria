package br.com.joaoapps.faciplac.carona.service.firebase.push.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.componentes.chat.objects.Message;

/**
 * Created by joaov on 19/11/2017.
 */

public class ComunicationCaronaBody implements JsonDeserializer, Serializable {
    public static final int STEP_ONE_COMUNICATION = 0;
    public static final int STEP_TWO_ACCEPT = 1;
    public static final int STEP_TWO_DENIED = 2;
    public static final int RECEIVE_MESSAGE = 3;

    private Integer step;
    private CaronaUsuario myUser;
    private CaronaUsuario otherUser;
    private StatusCarona statusCarona;
    private String message;

    public ComunicationCaronaBody(int step, CaronaUsuario myUser, CaronaUsuario otherUser, StatusCarona statusCarona) {
        this.myUser = myUser;
        this.otherUser = otherUser;
        this.step = step;
        this.statusCarona = statusCarona;
    }

    public ComunicationCaronaBody(Integer step, CaronaUsuario myUser, CaronaUsuario otherUser, String message) {
        this.step = step;
        this.myUser = myUser;
        this.otherUser = otherUser;
        this.message = message;
    }

    public ComunicationCaronaBody(Map<String, String> data) {
        Gson gson = new GsonBuilder().setLenient().create();
        this.otherUser = gson.fromJson(data.get("myUser"), CaronaUsuario.class);
        this.myUser = gson.fromJson(data.get("otherUser"), CaronaUsuario.class);
        this.statusCarona = (gson.fromJson(data.get("statusCarona"), StatusCarona.class));
        this.step = gson.fromJson(data.get("step"), Integer.class);
        this.message = data.get("message");
    }

    private StatusCarona getInverse(StatusCarona statusCarona) {
        if (statusCarona == StatusCarona.DAR_CARONA) {
            return StatusCarona.RECEBER_CARONA;
        } else {
            return StatusCarona.DAR_CARONA;
        }
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public CaronaUsuario getMyUser() {
        return myUser;
    }

    public void setMyUser(CaronaUsuario myUser) {
        this.myUser = myUser;
    }

    public CaronaUsuario getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(CaronaUsuario otherUser) {
        this.otherUser = otherUser;
    }

    public StatusCarona getStatusCarona() {
        return statusCarona;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCarona(StatusCarona statusCarona) {
        this.statusCarona = statusCarona;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement element = json.getAsJsonObject();
        if (json.getAsJsonObject() != null) {
            element = json.getAsJsonObject();
        }
        return (new Gson().fromJson(element, ComunicationCaronaBody.class));
    }
}
