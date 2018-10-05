package br.com.joaoapps.faciplac.carona.service.firebase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.firebase.client.core.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.joaoapps.faciplac.carona.service.exceptions.Code;
import br.com.joaoapps.faciplac.carona.service.listeners.OnLogarListener;
import br.com.joaoapps.faciplac.carona.service.listeners.OnRefreshAlunos;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.service.listeners.OnResetSenha;
import br.com.joaoapps.faciplac.carona.service.listeners.OnTransacaoListener;
import br.com.joaoapps.faciplac.carona.view.utils.Mask;


/**
 * Created by joaov on 07/11/2017.
 */

public class UsuarioFirebase {
    private static final String REFERENCIA = "Alunos";
    private static DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference(REFERENCIA);
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference(REFERENCIA);

    public static void saveOrUpdate(Usuario usuario) {
        firebaseReference.child(Mask.decodeString(usuario.getCpf())).setValue(usuario);
    }

    public static void saveImageUser(final Usuario usuario, final Bitmap bitmap, final OnTransacaoListener onTransacaoListener) {
        final StorageReference mountainImagesRef = storageRef.child("/profiles/" + Mask.unmask(usuario.getCpf()) + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                onTransacaoListener.error(100);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateUserFromUrl(usuario, onTransacaoListener, uploadTask, mountainImagesRef);
            }
        });
    }

    private static void updateUserFromUrl(final Usuario usuario, final OnTransacaoListener onTransacaoListener, UploadTask uploadTask, final StorageReference ref) {
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    usuario.setUrlFoto("https://firebasestorage.googleapis.com" + downloadUri.getPath() + "?alt=media");
                    saveOrUpdate(usuario);
                    onTransacaoListener.success(null);
                } else {
                    onTransacaoListener.error(1);
                }
            }
        });


//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful() && task != null) {
//                    throw task.getException();
//                }
//                return storageRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    usuario.setUrlFoto(downloadUri.getPath());
//                    saveOrUpdate(usuario);
//                } else {
//                    onTransacaoListener.error(1);
//                }
//            }
//        });
    }


    public static void save(final Usuario usuario, final OnTransacaoListener onTransacaoListener) {

        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario p = postSnapshot.getValue(Usuario.class);
                    assert p != null;
                    if (usuario != null && usuario.getCpf().equals(p.getCpf())) {
                        onTransacaoListener.error(Code.LOGIN_EXISTE);
                        return;
                    }

                    if (usuario != null && usuario.getMatricula().equals(p.getMatricula())) {
                        onTransacaoListener.error(Code.MATRICULA_EXISTE);
                        return;
                    }

                    if (usuario != null && usuario.getEmail().equals(p.getEmail())) {
                        onTransacaoListener.error(Code.EMAIL_EXISTE);
                        return;
                    }
                }
                saveOrUpdate(usuario);
                onTransacaoListener.success(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getCode() == DatabaseError.NETWORK_ERROR) {
                    onTransacaoListener.error(Code.NETWORK_ERROR);
                }
            }
        });

    }

    public static void getAll(final OnRefreshAlunos onRefershAlunos) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> usuarios = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario p = postSnapshot.getValue(Usuario.class);
                    usuarios.add(p);
                }
                onRefershAlunos.getAll(usuarios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onRefershAlunos.error(databaseError.getCode());
            }
        };

        firebaseReference.addValueEventListener(postListener);
    }

    public static void verifyEmailMatriculaValid(final String email, final String matricula, final OnResetSenha onResetSenha) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario p = postSnapshot.getValue(Usuario.class);
                    if (p != null && p.getEmail() != null && p.getEmail().equals(email) && p.getMatricula().equals(matricula)) {
                        onResetSenha.success(p);
                        return;
                    }
                }
                onResetSenha.error(Code.MATRICULA_EMAIL_INCORRETOS);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onResetSenha.error(databaseError.getCode());
            }
        };

        firebaseReference.addListenerForSingleValueEvent(postListener);
    }

    public static void doLogin(final String login, final String senha, final OnLogarListener onLogarListener) {
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario p = postSnapshot.getValue(Usuario.class);
                    if ((p.getCpf().equals(login) || p.getMatricula().equals(login)) && senha.equalsIgnoreCase(p.getSenha())) {
                        onLogarListener.success(p);
                        return;
                    }
                }
                onLogarListener.error(Code.CPF_SENHA_INCORRETO);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError.getCode() == DatabaseError.NETWORK_ERROR) {
                    onLogarListener.error(Code.NETWORK_ERROR);
                }
            }
        });
    }

}
