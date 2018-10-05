package br.com.joaoapps.faciplac.carona.model;

import java.io.Serializable;
import java.util.Date;

import br.com.joaoapps.faciplac.carona.model.enums.Status;

/**
 * Created by joaov on 31/03/2017.
 */

public class Usuario implements Serializable {
    private String nome;
    private String cpf;
    private String matricula;
    private String senha;
    private Autenticado autenticado;
    private Status status;
    private Date dataCadastro;
    private String email;
    private String pushId;
    private String telefone;
    private String urlFoto;
    private LatLng positionActual;
    private LatLng positionResidence;

    public Usuario(String nome, String cpf, String telefone, String matricula, String curso, String senha, Autenticado autenticado, Status status) {
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.telefone = telefone;
        this.senha = senha;
        this.autenticado = autenticado;
        this.status = status;
    }

    public String getUrlFoto() {
        if (urlFoto == null) {
            urlFoto = "";
        }
        return urlFoto.replace("/profiles/", "%2Fprofiles%2F");
    }

    public LatLng getPositionActual() {
        return positionActual;
    }

    public void setPositionActual(LatLng positionActual) {
        this.positionActual = positionActual;
    }

    public LatLng getPositionResidence() {
        return positionResidence;
    }

    public void setPositionResidence(LatLng positionResidence) {
        this.positionResidence = positionResidence;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String celular) {
        this.telefone = celular;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Usuario() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Autenticado getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(Autenticado autenticado) {
        this.autenticado = autenticado;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
