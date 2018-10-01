package br.com.joaoapps.faciplac.carona.service.exceptions;

import br.com.joaoapps.faciplac.carona.service.listeners.OnResetSenha;

/**
 * Created by joaov on 09/11/2017.
 */

public class Code {
    public static final int LOGIN_EXISTE = 1;
    public static final int MATRICULA_EXISTE = 2;
    public static final int NETWORK_ERROR = 3;
    public static final int CPF_SENHA_INCORRETO = 4;
    public static final int EMAIL_EXISTE = 5;
    public static final int MATRICULA_EMAIL_INCORRETOS = 6;
    public static final int ERRO_ENVIAR_EMAIL = 7;
    public static final int ERRO_AO_ENVIAR_PUSH = 8;
    public static final int ERRO_AO_BUSCAR_USUARIOS_CARONA = 9 ;
}
