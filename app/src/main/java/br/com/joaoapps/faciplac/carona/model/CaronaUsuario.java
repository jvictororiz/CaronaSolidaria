package br.com.joaoapps.faciplac.carona.model;


import java.io.Serializable;

import br.com.joaoapps.faciplac.carona.SuperApplication;
import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

/**
 * Created by joaov on 20/11/2017.
 */

public class CaronaUsuario implements Serializable {
    private String cpfUsuario;
    private double latitude;
    private double longitude;
    private String nome;
    private String pushId;
    private String celular;
    private StatusCarona statusCarona;
    private LatLng positionResidence;
    private String urlFoto;

    public CaronaUsuario() {
        //Não remover, firebase precisa
    }

    public CaronaUsuario(LatLng positionResidence, String pushId, String cpfUsuario, String urlFoto, String celular, double latitude, double longitude, String nome, StatusCarona statusCarona) {
        this.cpfUsuario = cpfUsuario;
        this.celular = celular;
        this.urlFoto = urlFoto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
        this.positionResidence = positionResidence;
        this.pushId = pushId;
        this.statusCarona = statusCarona;
    }

    public String getPushId() {
        String token = Preferences.getTockenId(SuperApplication.getInstance());
        if (token != null) {
            return token;
        }
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }


    public LatLng getPositionResidence() {
        return positionResidence;
    }

    public void setPositionResidence(LatLng positionResidence) {
        this.positionResidence = positionResidence;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusCarona getStatusCarona() {
        return statusCarona;
    }

    public void setStatusCarona(StatusCarona statusCarona) {
        this.statusCarona = statusCarona;
    }
}
