package br.com.joaoapps.faciplac.carona.service.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.joaoapps.faciplac.carona.model.CaronaUsuario;
import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.rest.OnEventListenerAbstract;
import br.com.joaoapps.faciplac.carona.view.utils.DateUtils;
import br.com.joaoapps.faciplac.carona.view.utils.Mask;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

/**
 * Created by joaov on 20/11/2017.
 */

public class CaronaUsuarioFirebase {
    private static DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("CaronaUsuario");
    private static ValueEventListener listenerGetAll;

    public static void openOrUpdate(Context context, CaronaUsuario user) {
        user.setDateLastLogin(new Date().getTime());
        Preferences.saveLastCpfLocgged(context, Mask.unmask(user.getCpfUsuario()));
        firebaseReference.child(Mask.decodeString(user.getCpfUsuario())).setValue(user);
    }

    public static void exit(Context context, String cpf) {
        if (cpf != null && !cpf.isEmpty()) {
            firebaseReference.child(Mask.decodeString(cpf)).setValue(null);
            Preferences.saveLastCpfLocgged(context, "");
        }
    }

    public static void isOnline(String cpf, OnEventListenerAbstract<Boolean> onEventListenerAbstract) {
        cpf = cpf.replaceAll("[.]-", "").replaceAll("-", "");
        firebaseReference.child(cpf.replace(".", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                CaronaUsuario caronaUsuario = snapshot.getValue(CaronaUsuario.class);
                if (caronaUsuario != null) {
                    onEventListenerAbstract.onSuccess(true);
                } else {
                    onEventListenerAbstract.onSuccess(false);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                onEventListenerAbstract.onError(117);
            }
        });
    }

    public static void getAll(Context context, final OnEventListenerAbstract<List<CaronaUsuario>> onRefreshCarona) {
        if (listenerGetAll == null) {
            listenerGetAll = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<CaronaUsuario> caronasUsuarios = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CaronaUsuario p = postSnapshot.getValue(CaronaUsuario.class);
                        if (p != null && p.getDateLastLogin() != 0 && !DateUtils.inAcceptableTime(new Date(p.getDateLastLogin()), 1)) {
                            exit(context, p.getCpfUsuario());
                        } else {
                            caronasUsuarios.add(p);
                        }
                    }
                    onRefreshCarona.onSuccess(caronasUsuarios);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    onRefreshCarona.onError(Code.ERRO_AO_BUSCAR_USUARIOS_CARONA);
                }
            };
            firebaseReference.addValueEventListener(listenerGetAll);
        }
    }

}
