package br.com.joaoapps.faciplac.carona.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joaov.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.Situacao;

public class AguardandoAprovacaoActivity extends AppCompatActivity {
    public final static String USUARIO ="Usuario";

    private TextView tvTitle;
    private TextView tvData;
    private ImageView imgAguardandoAprovacao;
    private ImageView imgAprovado;
    private Button btnClose;
    private TextView tvDesacordo;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aguardando_aprovacao);

        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvData = (TextView) findViewById(R.id.tv_data);
        imgAguardandoAprovacao = (ImageView) findViewById(R.id.img_aguardando_aprovacao);
        imgAprovado = (ImageView) findViewById(R.id.img_aprovado);
        btnClose = (Button) findViewById(R.id.btn_voltar);
        tvDesacordo = (TextView) findViewById(R.id.tv_desacordo);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(USUARIO);

        tvData.setText("22/04");

        if(usuario.getAutenticado() != null && (usuario.getAutenticado().getSituacao() != Situacao.APROVADO && usuario.getAutenticado().getNomeAutenticador() == null)){
            imgAguardandoAprovacao.setImageResource(R.drawable.wait);
             imgAprovado.setImageResource(R.drawable.wait);
            tvTitle.setText("AGUARDANDO APROVAÇÃO");
            tvDesacordo.setVisibility(View.GONE);
        }else {
            tvTitle.setText("SEU ACESSO FOI NEGADO");
            imgAguardandoAprovacao.setImageResource(R.drawable.correct);
            imgAprovado.setImageResource(R.drawable.negado);
            tvDesacordo.setVisibility(View.VISIBLE);
        }
    }
}
