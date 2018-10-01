package br.com.joaoapps.faciplac.carona.service.firebase.push.objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;

public class ComunicationCaronaRequest implements JsonDeserializer {
    ComunicationCaronaRequestBody comunicationCaronaRequestBody;
    String to;

    public ComunicationCaronaRequest(ComunicationCaronaRequestBody comunicationCaronaRequestBody, String to) {
        this.comunicationCaronaRequestBody = comunicationCaronaRequestBody;
        this.to = to;
    }

    public ComunicationCaronaRequestBody getComunicationCaronaRequestBody() {
        return comunicationCaronaRequestBody;
    }

    public void setComunicationCaronaRequestBody(ComunicationCaronaRequestBody comunicationCaronaRequestBody) {
        this.comunicationCaronaRequestBody = comunicationCaronaRequestBody;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
