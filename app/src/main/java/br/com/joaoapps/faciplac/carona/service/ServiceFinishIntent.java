package br.com.joaoapps.faciplac.carona.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import br.com.joaoapps.faciplac.carona.service.firebase.CaronaUsuarioFirebase;
import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

public class ServiceFinishIntent extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        String cpf = Preferences.getLastCpfLogged(this);

        if (cpf != null && !cpf.isEmpty()) {
            CaronaUsuarioFirebase.exit(this, cpf);
            super.onTaskRemoved(rootIntent);
        }
    }
}