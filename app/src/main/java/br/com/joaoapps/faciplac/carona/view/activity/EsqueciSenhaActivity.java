package br.com.joaoapps.faciplac.carona.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.service.email.EmailService;
import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;

public class EsqueciSenhaActivity extends AppCompatActivity {
    EditText edtMatricula;
    EditText edtEmail;
    Button btnRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        EmailService.loginEmail();

        edtMatricula = (EditText) findViewById(R.id.edt_matricula);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        btnRecuperar = (Button) findViewById(R.id.btn_recuperar);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioBO.resetSenha(EsqueciSenhaActivity.this, edtMatricula.getText().toString(), edtEmail.getText().toString());
            }
        });
    }
}
