package br.com.joaoapps.faciplac.carona.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;

import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.Situacao;
import br.com.joaoapps.faciplac.carona.view.utils.DateUtils;

public class AguardandoAprovacaoActivity extends SuperActivity {
    public final static String USUARIO = "USUARIO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aguardando_aprovacao);

        TextView tvTitle = findViewById(R.id.toolbar_title);
        TextView tvData = findViewById(R.id.tv_data);
        TextView tvTextNegado = findViewById(R.id.tv_message_negado);
        ImageView imgAguardandoAprovacao = findViewById(R.id.img_aguardando_aprovacao);
        ImageView imgAprovado = findViewById(R.id.img_aprovado);
        Button btnClose = findViewById(R.id.btn_voltar);
        TextView tvDesacordo = findViewById(R.id.tv_desacordo);

        btnClose.setOnClickListener(view -> finish());


        Intent intent = getIntent();
        Usuario usuario = (Usuario) intent.getSerializableExtra(USUARIO);

        tvData.setText(DateUtils.toString("MM/yyyy", usuario.getAutenticado().getDateAutenticacao()));

        if (usuario.getAutenticado() != null && (usuario.getAutenticado().getSituacao() != Situacao.APROVADO && usuario.getAutenticado().getNomeAutenticador() == null)) {
            tvTextNegado.setVisibility(View.GONE);
            imgAguardandoAprovacao.setImageResource(R.drawable.wait);
            imgAprovado.setImageResource(R.drawable.wait);
            tvTitle.setText("AGUARDANDO APROVAÇÃO");
            tvDesacordo.setVisibility(View.GONE);
        } else {
            tvTitle.setText("SEU ACESSO FOI NEGADO");
            tvTextNegado.setVisibility(View.VISIBLE);
            if (usuario.getAutenticado().getMotivoNegado() != null && !usuario.getAutenticado().getMotivoNegado().isEmpty()) {
                tvTextNegado.setText(usuario.getAutenticado().getMotivoNegado());
            } else {
                tvTextNegado.setVisibility(View.GONE);
            }
            imgAguardandoAprovacao.setImageResource(R.drawable.correct);
            imgAprovado.setImageResource(R.drawable.negado);
            tvDesacordo.setVisibility(View.VISIBLE);
        }
    }
}
