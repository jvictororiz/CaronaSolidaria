package br.com.joaoapps.faciplac.carona.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.service.email.EmailService;
import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;

public class EsqueciSenhaActivity extends SuperActivity {
    EditText edtMatricula;
    EditText edtEmail;
    Button btnRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);
        setupToolbar("Recuperar Senha");

        edtMatricula =  findViewById(R.id.edt_matricula);
        edtEmail =  findViewById(R.id.edt_email);
        btnRecuperar =  findViewById(R.id.btn_recuperar);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().isEmpty()){
                    AlertUtils.showAlert("Preencha o campo de e-mail", EsqueciSenhaActivity.this);
                    return;
                }
                if (edtMatricula.getText().toString().isEmpty()) {
                    AlertUtils.showAlert("Preencha o campo de matrícula", EsqueciSenhaActivity.this);
                    return;
                }
                UsuarioBO.resetSenha(EsqueciSenhaActivity.this, edtMatricula.getText().toString(), edtEmail.getText().toString());
            }
        });
    }
}
