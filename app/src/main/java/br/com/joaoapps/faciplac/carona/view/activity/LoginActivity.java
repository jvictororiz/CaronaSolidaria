package br.com.joaoapps.faciplac.carona.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;
import br.com.joaoapps.faciplac.carona.view.activity.cadastro.CadastroActivity;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

public class LoginActivity extends AppCompatActivity {
    EditText edtLogin;
    EditText edtSenha;
    Button btnLogar;
    TextView tvCadastrar;
    TextView tvEsqueciSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin =  findViewById(R.id.edt_login);
        edtSenha = findViewById(R.id.edt_senha);
        btnLogar =  findViewById(R.id.btn_logar);
        tvCadastrar = findViewById(R.id.tv_cadastrar);
        tvEsqueciSenha = findViewById(R.id.tv_esqueci_senha);

        edtLogin.setText(Preferences.getLastLogin(this));
        if (!edtLogin.getText().toString().isEmpty()) {
            edtSenha.requestFocus();
        }


        tvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            }
        });

        tvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, EsqueciSenhaActivity.class));
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = edtLogin.getText().toString();
                String senha = edtSenha.getText().toString();
                if (login.isEmpty()) {
                    edtLogin.setError("Campo não pode ficar vazio");
                } else if (senha.isEmpty()) {
                    edtSenha.setError("Campo não pode ficar vazio");
                } else {
                    UsuarioBO.doLogin(login, senha, LoginActivity.this);
                    Preferences.saveLastLogin(LoginActivity.this, login);
                    edtSenha.setText("");
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
