package br.com.joaoapps.faciplac.carona.model;

import java.io.Serializable;
import java.util.Date;

import br.com.joaoapps.faciplac.carona.model.enums.Situacao;

/**
 * Created by joaov on 07/11/2017.
 */

public class Autenticado implements Serializable {
    private Situacao situacao;
    private Date dateAutenticacao;
    private String nomeAutenticador;
    private String motivoNegado;

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public String getMotivoNegado() {
        return motivoNegado;
    }

    public void setMotivoNegado(String motivoNegado) {
        this.motivoNegado = motivoNegado;
    }

    public Autenticado(Situacao situacao) {
        this.situacao = situacao;
    }

    public Autenticado(Situacao situacao, Date dateAutenticacao, String nomeAutenticador) {
        this.situacao = situacao;
        this.dateAutenticacao = dateAutenticacao;
        this.nomeAutenticador = nomeAutenticador;
    }

    public Autenticado(Situacao situacao, Date dateAutenticacao, String nomeAutenticador, String motivoNegado) {
        this.situacao = situacao;
        this.motivoNegado = motivoNegado;
        this.dateAutenticacao = dateAutenticacao;
        this.nomeAutenticador = nomeAutenticador;
    }


    public Autenticado() {
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setAutenticado(Situacao autenticado) {
        situacao = autenticado;
    }

    public Date getDateAutenticacao() {
        return dateAutenticacao;
    }

    public void setDateAutenticacao(Date dateAutenticacao) {
        this.dateAutenticacao = dateAutenticacao;
    }

    public String getNomeAutenticador() {
        return nomeAutenticador;
    }

    public void setNomeAutenticador(String nomeAutenticador) {
        this.nomeAutenticador = nomeAutenticador;
    }
}
