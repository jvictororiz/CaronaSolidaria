package br.com.joaoapps.faciplac.carona.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import br.com.joaoapps.faciplac.carona.model.enums.StatusCarona;

/**
 * Created by joaov on 20/11/2017.
 */

public class CaronaUsuario implements Serializable {
    private String cpfUsuario;
    private double latitude;
    private double longitude;
    private String nome;
    private String valorCarona;
    private String pushId;
    private String telefone;
    private StatusCarona statusCarona;
    private String urlFoto;

    public CaronaUsuario() {
        //Não remover, firebase precisa
    }

    public CaronaUsuario(String pushId, String cpfUsuario, String urlFoto, String telefone, double latitude, double longitude, String nome, String valorCarona, StatusCarona statusCarona) {
        this.cpfUsuario = cpfUsuario;
        this.telefone = telefone;
        this.urlFoto = urlFoto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
        this.pushId = pushId;
        this.valorCarona = valorCarona;
        this.statusCarona = statusCarona;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getValorCarona() {
        if (valorCarona == null) {
            valorCarona = "";
        }
        return valorCarona;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setValorCarona(String valorCarona) {
        this.valorCarona = valorCarona;
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
