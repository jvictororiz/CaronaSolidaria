package br.com.joaoapps.faciplac.carona.service.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.listeners.OnRefreshAlunos;
import br.com.joaoapps.faciplac.carona.service.listeners.OnRefreshUsuarisCaronas;
import br.com.joaoapps.faciplac.carona.view.utils.Mask;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

/**
 * Created by joaov on 20/11/2017.
 */

public class CaronaUsuarioFirebase {
    private static DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("CaronaUsuario");

    public static void openOrUpdate(Context context, CaronaUsuario usuario) {
        Preferences.saveLastCpfLocgged(context, Mask.unmask(usuario.getCpfUsuario()));
        firebaseReference.child(Mask.decodeString(usuario.getCpfUsuario())).setValue(usuario);
    }

    public static void exit(Context context, String cpf) {
        if (cpf != null && !cpf.isEmpty()) {
            firebaseReference.child(Mask.decodeString(cpf)).setValue(null);
            Preferences.saveLastCpfLocgged(context, "");
        }
    }

    public static void getAll(final OnRefreshUsuarisCaronas onRefreshCarona) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CaronaUsuario> caronasUsuarios = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CaronaUsuario p = postSnapshot.getValue(CaronaUsuario.class);
                    caronasUsuarios.add(p);
                }
                onRefreshCarona.onSuccess(caronasUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onRefreshCarona.onError(Code.ERRO_AO_BUSCAR_USUARIOS_CARONA);
            }
        };

        firebaseReference.addListenerForSingleValueEvent(postListener);
    }

}
