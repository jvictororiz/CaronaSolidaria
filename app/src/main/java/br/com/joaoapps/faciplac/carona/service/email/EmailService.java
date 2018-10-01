package br.com.joaoapps.faciplac.carona.service.email;
import android.app.Activity;
import android.os.Bundle;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.listeners.OnTransacaoListener;

public class EmailService {
    private static Session session = null;
    String emailDestinatario;

    public static void loginEmail(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("caronasolidariaapp@gmail.com", "joao.456");
            }
        });

    }

    public static  void resetSenha(String email, String senha, OnTransacaoListener onTransacaoListener){
        RetreiveFeedTask task = new RetreiveFeedTask(onTransacaoListener);
        task.execute(email, senha);
    }

    static class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        OnTransacaoListener onTransacaoListener;

        public RetreiveFeedTask(OnTransacaoListener onTransacaoListener) {
            this.onTransacaoListener = onTransacaoListener;
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params[0]));
                message.setSubject("Solicitação de senha Faciplac CaronaUsuario");
                message.setContent("Sua senha é "+ params[1]+", guarde-a bem", "text/html; charset=utf-8");
                Transport.send(message);
                onTransacaoListener.success(null);
            } catch(MessagingException e) {
                onTransacaoListener.error(Code.ERRO_ENVIAR_EMAIL);
            } catch(Exception e) {
                onTransacaoListener.error(Code.ERRO_ENVIAR_EMAIL);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("","email enviado");
        }
    }
}