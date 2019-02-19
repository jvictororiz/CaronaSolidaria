package br.com.joaoapps.faciplac.carona.view.activity.bo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import br.com.joaoapps.faciplac.carona.SuperApplication;
import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.Situacao;
import br.com.joaoapps.faciplac.carona.model.enums.Status;
import br.com.joaoapps.faciplac.carona.service.email.EmailService;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.firebase.UsuarioFirebase;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.Notification;
import br.com.joaoapps.faciplac.carona.service.listeners.OnEventListener;
import br.com.joaoapps.faciplac.carona.service.listeners.OnLogarListener;
import br.com.joaoapps.faciplac.carona.service.listeners.OnResetSenha;
import br.com.joaoapps.faciplac.carona.service.rest.UsuarioRestService;
import br.com.joaoapps.faciplac.carona.view.activity.AguardandoAprovacaoActivity;
import br.com.joaoapps.faciplac.carona.view.activity.AlunosPreCadastradosActivity;
import br.com.joaoapps.faciplac.carona.view.activity.LoginActivity;
import br.com.joaoapps.faciplac.carona.view.activity.RegistroLocalizacaoActivity;
import br.com.joaoapps.faciplac.carona.view.activity.SuperActivity;
import br.com.joaoapps.faciplac.carona.view.activity.cadastro.CadastroActivity;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;

/**
 * Created by joaov on 10/11/2017.
 */

public class UsuarioBO {

    public static void doLogin(String login, String senha, final AppCompatActivity context) {
        SuperActivity.showDialogLoad(context);
        UsuarioFirebase.doLogin(login, senha, new OnLogarListener() {
            @Override
            public void success(Usuario usuario) {
                SuperActivity.closeDialogLoad();
                String tocken = FirebaseInstanceId.getInstance().getToken();
                if (tocken != null) {
                    usuario.setPushId(tocken);
                    UsuarioFirebase.saveOrUpdate(usuario);
                }
                if (usuario.getStatus() == Status.ALUNO) {
                    if (usuario.getAutenticado().getSituacao() == Situacao.APROVADO) {
                        Intent intent = new Intent(context, RegistroLocalizacaoActivity.class);
                        intent.putExtra("USUARIO", usuario);
                        SuperActivity.startActivityMessagePositive(context, intent, "Login realizado\ncom sucesso !");
                    } else {
                        Intent intent = new Intent(context, AguardandoAprovacaoActivity.class);
                        intent.putExtra(AguardandoAprovacaoActivity.USUARIO, usuario);
                        SuperActivity.startActivityMessagePositive(context, intent, "Login realizado\ncom sucesso !");
                    }
                } else if (usuario.getStatus() == Status.DIRETOR || usuario.getStatus() == Status.SUB_DIRETOR) {
                    Intent intent = new Intent(context, AlunosPreCadastradosActivity.class);
                    intent.putExtra("USUARIO", usuario);
                    SuperActivity.startActivityMessagePositive(context, intent, "Login realizado\ncom sucesso !");
                    SuperApplication.setUsuarioLogado(usuario);
                    SuperActivity.closeDialogLoad();
                } else {
                    SuperActivity.closeDialogLoad();
                    AlertUtils.showAlert("Login ou senha incorretos !", context);
                }
            }

            @Override
            public void error(int code) {
                SuperActivity.closeDialogLoad();
                if (code == Code.CPF_SENHA_INCORRETO) {
                    AlertUtils.showAlert("Login ou senha incorretos !", context);
                }
            }
        });
    }

    public static void editUser(final Bitmap bitmap, final Usuario usuario, final AppCompatActivity context) {
        SuperActivity.showDialogLoad(context);
        UsuarioFirebase.saveOrUpdate(usuario);
        if (bitmap != null) {
            UsuarioFirebase.saveImageUser(usuario, bitmap, new OnEventListener() {
                @Override
                public void success(Object object) {
                    SuperActivity.closeDialogLoad();
                    context.setResult(CadastroActivity.USER_CODE, new Intent().putExtra("USER", usuario));
                    context.finish();
                }

                @Override
                public void error(int code) {
                    SuperActivity.closeDialogLoad();
                    context.finish();
                }
            });
        } else {
            context.setResult(CadastroActivity.USER_CODE, new Intent().putExtra("USER", usuario));
            context.finish();
            SuperActivity.closeDialogLoad();
        }
    }

    public static void registerOrEditUser(final Bitmap bitmap, final Usuario usuario, final AppCompatActivity context) {
        SuperActivity.showDialogLoad(context);
        final Intent intent = new Intent(context, AguardandoAprovacaoActivity.class);
        intent.putExtra(AguardandoAprovacaoActivity.USUARIO, usuario);

        UsuarioFirebase.save(usuario, new OnEventListener() {
            @Override
            public void success(Object o) {
                SuperActivity.closeDialogLoad();
                if (bitmap != null) {
                    saveImageUser(context, usuario, bitmap);
                }
                SuperActivity.startActivityMessagePositive(context, intent, "Cadaastro realizado \ncom sucesso !");
                context.finish();
            }

            @Override
            public void error(int code) {
                SuperActivity.closeDialogLoad();
                if (code == Code.LOGIN_EXISTE) {
                    AlertUtils.showAlert("CPF já está cadastrado !", context);
                } else if (code == Code.NETWORK_ERROR) {
                    AlertUtils.showAlert("Sem internet", context);
                } else if (code == Code.MATRICULA_EXISTE) {
                    AlertUtils.showAlert("Matrícula já está cadastrada", context);
                } else if (code == Code.EMAIL_EXISTE) {
                    AlertUtils.showAlert("E-mail já está cadastrado", context);
                } else {
                    AlertUtils.showAlert("Erro inesperado", context);
                }

            }
        });
    }

    public static void saveImageUser(final Context context, Usuario usuario, Bitmap bitmap) {
        UsuarioFirebase.saveImageUser(usuario, bitmap, new OnEventListener() {
            @Override
            public void success(Object object) {
            }

            @Override
            public void error(int code) {
            }
        });
    }

    public static void resetSenha(final AppCompatActivity appCompatActivity, String matricula, final String email) {
        SuperActivity.showDialogLoad(appCompatActivity);
        UsuarioFirebase.verifyEmailMatriculaValid(email, matricula, new OnResetSenha() {
            @Override
            public void success(Usuario usuario) {
                EmailService.loginEmail();
                EmailService.resetSenha(email, usuario.getSenha(), new OnEventListener() {
                    @Override
                    public void success(Object o) {
                        SuperActivity.closeDialogLoad();
                        SuperActivity.startActivityMessagePositive(appCompatActivity, new Intent(appCompatActivity, LoginActivity.class), "Um e-mail com a sua senha foi enviado");
                        appCompatActivity.finish();
                    }

                    @Override
                    public void error(int code) {
                        SuperActivity.closeDialogLoad();
                        if (code == Code.ERRO_ENVIAR_EMAIL) {
                            AlertUtils.showAlert("Erro ao enviar e-mail. Favor procurar a secretaria para regularização de sua conta.", appCompatActivity);
                        }
                    }
                });

            }

            @Override
            public void error(int code) {
                SuperActivity.closeDialogLoad();
                if (code == Code.MATRICULA_EMAIL_INCORRETOS) {
                    AlertUtils.showAlert("Matricula ou e-mail não correspondem a nenhuma conta", appCompatActivity);
                }
            }
        });
    }

    public static void sendFeedbackDiretoria(final AppCompatActivity appCompatActivity, final Usuario usuario, Notification notification) {
        UsuarioFirebase.saveOrUpdate(usuario);
        UsuarioRestService notificationRestService = new UsuarioRestService(appCompatActivity);
        notification.setPushIdRemetente(usuario.getPushId());
        notificationRestService.sendNotification(notification, new OnEventListener() {
            @Override
            public void success(Object object) {
                if (usuario.getAutenticado().getSituacao() == Situacao.APROVADO) {
                    AlertUtils.showInfo(usuario.getNome().concat(" foi notificado do sucesso de seu cadastro."), appCompatActivity);
                } else if (usuario.getAutenticado().getSituacao() == Situacao.NEGADO) {
                    AlertUtils.showInfo(usuario.getNome().concat(" foi notificado a respeito da invalidez de seu cadastro."), appCompatActivity);
                }
            }

            @Override
            public void error(int code) {

            }
        });
    }

    public static void pedirCarona(CaronaUsuario myUser, CaronaUsuario otherUser) {

    }

    public static void oferecerCarona() {

    }
}
