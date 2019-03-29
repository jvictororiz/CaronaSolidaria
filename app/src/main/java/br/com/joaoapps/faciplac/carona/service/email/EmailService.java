package br.com.joaoapps.faciplac.carona.service.email;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.rest.OnEventListenerAbstract;

public class EmailService {
    private static Session session = null;
    private final static String EMAIL_CARONA = "caronasolidariaapp@gmail.com";

    public static void loginEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_CARONA, "joao.456");
            }
        });
        session.setDebug(true);

    }

    public static void resetSenha(String email, String senha, OnEventListenerAbstract<Void> onEventListener) {
        RetreiveFeedTask task = new RetreiveFeedTask(onEventListener);
        task.execute(email, senha);
    }

    static class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        OnEventListenerAbstract<Void> onEventListener;

        RetreiveFeedTask(OnEventListenerAbstract<Void> onEventListener) {
            this.onEventListener = onEventListener;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
//                Message message = new MimeMessage(session);
//                message.setFrom(new InternetAddress(params[0]));
//                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params[0]));
//                message.setSubject("Solicitação de senha Faciplac Saidão Faciplac");
//                message.setContent("Sua senha é " + params[1] + ", guarde-a bem", "text/html; charset=utf-8");
//                Transport.send(message);

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_CARONA)); //Remetente
                Address[] toUser = InternetAddress.parse(params[0]);
                message.setRecipients(Message.RecipientType.TO, toUser);
                message.setSubject("Solicitação de senha Faciplac Saidão Faciplac");
                message.setText("Sua senha é " + params[1] + ", guarde-a bem");
                Transport.send(message);
                onEventListener.onSuccess(null);
            } catch (Exception e) {
                onEventListener.onError(Code.ERRO_ENVIAR_EMAIL);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("", "email enviado");
        }
    }
}