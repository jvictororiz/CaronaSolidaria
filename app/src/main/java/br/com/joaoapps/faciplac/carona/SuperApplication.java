package br.com.joaoapps.faciplac.carona;

import android.app.Application;
import android.content.Intent;
import android.location.Location;

import com.joaov.faciplac.caronasolidaria.R;

import java.util.Locale;

import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.service.ServiceFinishIntent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by joao.roriz on 29/03/18.
 */

public class SuperApplication extends Application {

    private static SuperApplication instance;
    private static Usuario usuarioLogado;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        try {
            startService(new Intent(getBaseContext(), ServiceFinishIntent.class));
        } catch (Exception ignore) {

        }
    }

    public static Locale getLocale() {
        return new Locale("pt", "BR");
    }

    public static SuperApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Configure o AndroidManifest.xml");
        }
        return instance;
    }

    public static void setInstance(SuperApplication instance) {
        SuperApplication.instance = instance;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        SuperApplication.usuarioLogado = usuarioLogado;
    }
}
