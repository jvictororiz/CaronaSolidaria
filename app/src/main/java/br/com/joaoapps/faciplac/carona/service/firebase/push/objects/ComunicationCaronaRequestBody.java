package br.com.joaoapps.faciplac.carona.service.firebase.push.objects;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;

/**
 * Created by joaov on 19/11/2017.
 */

public class ComunicationCaronaRequestBody implements JsonDeserializer {

    private CaronaUsuario myUser;
    private CaronaUsuario otherUser;
    private StatusCarona statusCarona;

    public ComunicationCaronaRequestBody(CaronaUsuario myUser, CaronaUsuario otherUser, StatusCarona statusCarona) {
        this.myUser = myUser;
        this.otherUser = otherUser;
        this.statusCarona = statusCarona;
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

    public void setStatusCarona(StatusCarona statusCarona) {
        this.statusCarona = statusCarona;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement element = json.getAsJsonObject();
        if (json.getAsJsonObject() != null) {
            element = json.getAsJsonObject();
        }
        return (new Gson().fromJson(element, ComunicationCaronaRequestBody.class));
    }
}
