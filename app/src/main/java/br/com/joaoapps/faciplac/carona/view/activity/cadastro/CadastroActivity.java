package br.com.joaoapps.faciplac.carona.view.activity.cadastro;

import android.app.Activity;
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

    public static final int USER_CODE = 100;
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
    private boolean isEdition;

    public static void start(Activity context, Usuario usuario) {
        Intent starter = new Intent(context, CadastroActivity.class);
        starter.putExtra("USUARIO", usuario);
        context.startActivityForResult(starter, CadastroActivity.USER_CODE);
    }

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
        edtTelefone = findViewById(R.id.edt_celular);
        attachProfileView = findViewById(R.id.attach_profile);
        setConfigs();

        usuario = (Usuario) getIntent().getSerializableExtra("USUARIO");
        isEdition = usuario != null;
        if (isEdition) {
            setUserInFilds();
        }


    }

    private void setUserInFilds() {
        edtNome.setText(usuario.getNome());
        edtCpf.setText(usuario.getCpf());
        edtEmail.setText(usuario.getEmail());
        edtTelefone.setText(usuario.getTelefone());
        edtMatricula.setText(usuario.getMatricula());
        attachProfileView.config(this);
        attachProfileView.setImage(usuario.getUrlFoto());
        edtCpf.setEnabled(false);
        edtCpf.setAlpha(0.3f);
        edtMatricula.setEnabled(false);
        edtMatricula.setAlpha(0.3f);
        findViewById(R.id.input_confirm_password).setVisibility(View.GONE);
        findViewById(R.id.input_password).setVisibility(View.GONE);

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
            AlertUtils.showAlert("CPF inválido", this);
            return;
        }
        if (!validateEmail()) {
            AlertUtils.showAlert("E-mail inválido", this);
            return;
        }


        if (!attachProfileView.containsBitmap()) {
            AlertUtils.showAlert("Você deve inserir uma foto para a confirmação de usuário", this);
            return;
        }
        setFieldInValues();
        if (!isEdition) {
            if (validatePasswords()) {
                AlertUtils.showAlert("Senhas não correspondem", this);
                return;
            }

            if (!validateForcePassword()) {
                AlertUtils.showAlert("Senha deve ter no mínimo 4 caracteres", this);
                return;
            }
            UsuarioBO.registerOrEditUser(attachProfileView.getBitmap(), usuario, this);
        } else {
            Bitmap bitmap = attachProfileView.changedBitmap() ? attachProfileView.getBitmap() : null;
            UsuarioBO.editUser(bitmap, usuario, this);
        }

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
        if (!isEdition) {
            return (!TextUtils.isEmpty(edtNome.getText().toString())
                    && !TextUtils.isEmpty(edtCpf.getText().toString())
                    && !TextUtils.isEmpty(edtMatricula.getText().toString())
                    && !TextUtils.isEmpty(edtEmail.getText().toString())
                    && !TextUtils.isEmpty(Mask.unmask(edtTelefone.getText().toString()))
                    && !TextUtils.isEmpty(edtSenha.getText().toString())
                    && !TextUtils.isEmpty(edtConfirmaSenha.getText().toString()));
        } else {
            return !TextUtils.isEmpty(edtNome.getText().toString())
                    && !TextUtils.isEmpty(edtCpf.getText().toString())
                    && !TextUtils.isEmpty(edtMatricula.getText().toString())
                    && !TextUtils.isEmpty(edtEmail.getText().toString())
                    && !TextUtils.isEmpty(Mask.unmask(edtTelefone.getText().toString()));
        }
    }

    private void setFieldInValues() {
        if (usuario == null) {
            usuario = new Usuario();
        }
        usuario.setNome(edtNome.getText().toString());
        usuario.setEmail(edtEmail.getText().toString());
        usuario.setTelefone(edtTelefone.getText().toString());
        usuario.setPushId(FirebaseInstanceId.getInstance().getId());
        if (!isEdition) {
            usuario.setSenha(edtSenha.getText().toString());
            usuario.setStatus(Status.ALUNO);
            usuario.setAutenticado(new Autenticado(Situacao.ESPERA));
            usuario.setCpf(edtCpf.getText().toString());
            usuario.setMatricula(edtMatricula.getText().toString());
            usuario.setDataCadastro(new Date());
        }
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
