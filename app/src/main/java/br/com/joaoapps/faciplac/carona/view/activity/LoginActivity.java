package br.com.joaoapps.faciplac.carona.view.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.joaov.faciplac.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;
import br.com.joaoapps.faciplac.carona.view.activity.cadastro.CadastroActivity;
import br.com.joaoapps.faciplac.carona.view.utils.Preferences;

public class LoginActivity extends SuperActivity {
    private EditText edtLogin;
    private EditText edtSenha;
    private Button btnLogar;
    private TextView tvCadastrar;
    private TextView tvEsqueciSenha;
    private TextView tvErro;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = findViewById(R.id.edt_login);
        edtSenha = findViewById(R.id.edt_password);
        btnLogar = findViewById(R.id.btn_login);
        tvCadastrar = findViewById(R.id.tv_register);
        tvEsqueciSenha = findViewById(R.id.tv_repassword);
        tvErro = findViewById(R.id.tv_error);
        animationView = findViewById(R.id.animation_view);
        configAnimationView();

        edtLogin.setOnFocusChangeListener(getTextWatcherAnimation());
        edtSenha.setOnFocusChangeListener(getTextWatcherAnimation());

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
                animationView.setProgress(0f);
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
    protected void onResume() {
        super.onResume();
        if (edtLogin.isFocused()) {
            showEyesAnimation();
        } else {
            closeEyesAnimation();
        }
    }

    private void configAnimationView() {
        animationView.setMinFrame(0);
        animationView.setMaxFrame(10);
        animationView.setMinProgress(0f);
        animationView.setMaxProgress(1f);

    }

    private View.OnFocusChangeListener getTextWatcherAnimation() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (edtLogin.isFocused()) {
                    showEyesAnimation();
                } else {
                    closeEyesAnimation();
                }
            }
        };
    }

    private void closeEyesAnimation() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView.setProgress(valueAnimator.getAnimatedFraction());
                if (valueAnimator.getAnimatedFraction() > 0.6f && valueAnimator.getAnimatedFraction() < 0.7f) {
                    animator.cancel();
                }
            }
        });
        animator.start();
    }

    private void showEyesAnimation() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1f).setDuration(700);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView.setProgress(0.6f - valueAnimator.getAnimatedFraction());
//                if (valueAnimator.getAnimatedFraction() > 0.6f && valueAnimator.getAnimatedFraction() < 0.7f) {
//                    animator.cancel();
//                }
            }
        });
        animator.start();
    }
}
