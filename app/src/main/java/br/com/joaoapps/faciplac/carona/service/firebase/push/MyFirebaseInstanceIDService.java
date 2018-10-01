package br.com.joaoapps.faciplac.carona.service.firebase.push;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

/**
 * Created by joaov on 18/11/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Preferences.saveTockenId(getApplicationContext(), token);

    }
}
