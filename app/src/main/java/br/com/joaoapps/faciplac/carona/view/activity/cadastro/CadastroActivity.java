package br.com.joaoapps.faciplac.carona.view.activity.cadastro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.joaov.caronasolidaria.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import br.com.joaoapps.faciplac.carona.model.Autenticado;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.Situacao;
import br.com.joaoapps.faciplac.carona.model.enums.Status;
import br.com.joaoapps.faciplac.carona.view.activity.SuperActivity;
import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;
import br.com.joaoapps.faciplac.carona.view.componentes.attach.AttachProfileView;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;
import br.com.joaoapps.faciplac.carona.view.utils.Mask;
import br.com.joaoapps.faciplac.carona.view.utils.ValidaCPF;

public class CadastroActivity extends SuperActivity {

    private EditText edtNome;
    private EditText edtCpf;
    private EditText edtMatricula;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private EditText edtEmail;
    private EditText edtTelefone;
    private AttachProfileView attachProfileView;

    private Usuario usuario;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setupToolbar("Cadastro");

        edtNome = findViewById(R.id.edt_nome);
        edtCpf = findViewById(R.id.edt_cpf);
        edtMatricula = findViewById(R.id.edt_matricula);
        edtSenha = findViewById(R.id.edt_password);
        edtConfirmaSenha = findViewById(R.id.edt_confirma_senha);
        edtEmail = findViewById(R.id.edt_email);
        edtTelefone = findViewById(R.id.edt_telefone);
        attachProfileView = findViewById(R.id.attach_profile);
        setConfigs();


    }

    private void setConfigs() {
        edtCpf.addTextChangedListener(Mask.insert("###.###.###-##", edtCpf));
        edtTelefone.addTextChangedListener(Mask.insert("(##)# ####-#### ", edtTelefone));
        attachProfileView.config(this);
    }

    //CADASTRO  AQUI
    public void clickCadastrar(View view) {

        if (!validateFieldsEmpty()) {
            AlertUtils.showAlert("Preencha todos os campos", this);
            return;
        }

        if (!ValidaCPF.isCPF(edtCpf.getText().toString().replaceAll("[.]", "").replaceAll("[-]", ""))) {
            AlertUtils.showAlert("Cpf inválido", this);
            return;
        }
        if (!validateEmail()) {
            AlertUtils.showAlert("E-mail inválido", this);
            return;
        }

        if (validatePasswords()) {
            AlertUtils.showAlert("Senhas não correspondem", this);
            return;
        }

        if (!validateForcePassword()) {
            AlertUtils.showAlert("Senha deve ter no mínimo 4 caracteres", this);
            return;
        }

        if (!attachProfileView.containsBitmap()) {
            AlertUtils.showAlert("Você deve inserir uma foto para a confirmação de usuário", this);
            return;
        }

        setFieldInValues();
        UsuarioBO.registerUser(attachProfileView.getBitmap(), usuario, this);


    }

    private boolean validateForcePassword() {
        String password = edtConfirmaSenha.getText().toString();
        return password.length() >= 3;
    }

    public boolean validateEmail() {
        String email = edtEmail.getText().toString();
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean validatePasswords() {
        return !edtConfirmaSenha.getText().toString().equals(edtSenha.getText().toString());
    }

    private boolean validateFieldsEmpty() {
        return (!TextUtils.isEmpty(edtNome.getText().toString())
                && !TextUtils.isEmpty(edtCpf.getText().toString())
                && !TextUtils.isEmpty(edtMatricula.getText().toString())
                && !TextUtils.isEmpty(edtEmail.getText().toString())
                && !TextUtils.isEmpty(Mask.unmask(edtTelefone.getText().toString()))
                && !TextUtils.isEmpty(edtSenha.getText().toString())
                && !TextUtils.isEmpty(edtConfirmaSenha.getText().toString())

        );
    }

    private void setFieldInValues() {
        usuario = new Usuario();
        usuario.setNome(edtNome.getText().toString());
        usuario.setCpf(edtCpf.getText().toString());
        usuario.setStatus(Status.ALUNO);
        usuario.setAutenticado(new Autenticado(Situacao.ESPERA));
        usuario.setSenha(edtSenha.getText().toString());
        usuario.setMatricula(edtMatricula.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setTelefone(edtTelefone.getText().toString());
        usuario.setDataCadastro(new Date());
        usuario.setPushId(FirebaseInstanceId.getInstance().getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        try {
            if (requestCode == AttachProfileView.PICK_IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    bitmap = (Bitmap) extras2.get("data");
                }
            }

            if (requestCode == AttachProfileView.SEARCH_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
            if (bitmap != null) {
                attachProfileView.setImage(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
