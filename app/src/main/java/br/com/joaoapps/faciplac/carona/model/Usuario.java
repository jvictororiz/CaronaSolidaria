package br.com.joaoapps.faciplac.carona.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.joaoapps.faciplac.carona.model.enums.Status;

/**
 * Created by joaov on 31/03/2017.
 */

public class Usuario implements Serializable {
    private String nome;
    private String cpf;
    private String matricula;
    private String senha;
    private List<Autenticado> historicAutenticado;
    private Status status;
    private Date dataCadastro;
    private String email;
    private String pushId;
    private String telefone;
    private String urlFoto;

    public Usuario(String nome, String cpf, String telefone, String matricula, String curso, String senha, List<Autenticado> historicAutenticado, Status status) {
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.telefone = telefone;
        this.senha = senha;
        this.historicAutenticado = historicAutenticado;
        this.status = status;
    }

    public String getUrlFoto() {
        if (urlFoto == null) {
            urlFoto = "";
        }
        return urlFoto.replace("/profiles/", "%2Fprofiles%2F");
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public List<Autenticado> getHistoricAutenticado() {
        return historicAutenticado;
    }

    public Autenticado getLastAutenticado(){
        return historicAutenticado.get(historicAutenticado.size());
    }

    public void setHistoricAutenticado(List<Autenticado> historicAutenticado) {
        this.historicAutenticado = historicAutenticado;
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
