package br.com.joaoapps.faciplac.carona.view.activity.adapters;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joaov.faciplac.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import br.com.joaoapps.faciplac.carona.SuperApplication;
import br.com.joaoapps.faciplac.carona.model.Autenticado;
import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.Situacao;
import br.com.joaoapps.faciplac.carona.service.firebase.push.objects.Notification;
import br.com.joaoapps.faciplac.carona.view.activity.bo.UsuarioBO;
import br.com.joaoapps.faciplac.carona.view.utils.DateUtils;
import br.com.joaoapps.faciplac.carona.view.utils.DialogUtils;

/**
 * Created by joaov on 02/11/2017.
 */

public class UsuarioNaoAutenticadoAdapter extends RecyclerView.Adapter<UsuarioNaoAutenticadoAdapter.AlunoHolder> {

    private List<Usuario> usuarios;
    private AppCompatActivity context;

    public UsuarioNaoAutenticadoAdapter(List<Usuario> usuarios, AppCompatActivity context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public AlunoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alunos, parent, false);
        return new UsuarioNaoAutenticadoAdapter.AlunoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoHolder holder, final int position) {
        final Usuario usuario = usuarios.get(position);

        holder.tvNome.setText(usuario.getNome());
        holder.tvCpf.setText(usuario.getCpf());
        holder.tvMatricula.setText(usuario.getMatricula());
        holder.tvDataCadastro.setText(DateUtils.toString("dd/MM/yyyy", usuario.getDataCadastro()));

        if (usuario.getAutenticado().getSituacao() != Situacao.ESPERA) {
            holder.tvNomeAutenticador.setText(usuario.getAutenticado().getNomeAutenticador());
            holder.tvDataAutenticada.setText(DateUtils.toString("dd/MM/yyyy", usuario.getAutenticado().getDateAutenticacao()));
            if (usuario.getAutenticado().getSituacao() == Situacao.APROVADO) {
                holder.imgAutenticado.setImageResource(R.drawable.positive_joia_icon);
            }
            if (usuario.getAutenticado().getSituacao() == Situacao.NEGADO) {
                holder.imgAutenticado.setImageResource(R.drawable.negative_joia_icon);
            }

        } else {
            holder.bodyAutenticacao.setVisibility(View.GONE);
        }

        if (usuario.getUrlFoto() != null && !usuario.getUrlFoto().isEmpty()) {
            Picasso.with(context)
                    .load(usuario.getUrlFoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.icon_user_default)
                    .error(R.drawable.icon_user_default)
                    .into(holder.imgUser);
        }


        holder.btnAprovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialogConfirm(context, "Tem certeza que deseja aprovar o acesso de " + usuario.getNome() + " ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usuario.setAutenticado(new Autenticado(Situacao.APROVADO, new Date(), SuperApplication.getUsuarioLogado().getNome()));
                        Notification notification = new Notification("Faciplac Carona", "Seu acesso ao faciplac carona foi aprovado com sucesso");
                        notification.setPushIdRemetente(usuario.getPushId());
                        UsuarioBO.sendFeedbackDiretoria(context, usuario, notification);

                    }
                });

            }
        });

        holder.btnNeggar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialogConfirm(context, "Tem certeza que deseja negar o acesso de " + usuario.getNome() + " ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usuario.setAutenticado(new Autenticado(Situacao.NEGADO, new Date(), SuperApplication.getUsuarioLogado().getNome()));
                        Notification notification = new Notification("Faciplac Carona", "Seu acesso ao faciplac carona infelizmente foi negado");
                        notification.setPushIdRemetente(usuario.getPushId());
                        UsuarioBO.sendFeedbackDiretoria(context, usuario, notification);
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    class AlunoHolder extends RecyclerView.ViewHolder {
        TextView tvNome;
        TextView tvMatricula;
        CircleImageView imgUser;
        TextView tvCpf;
        TextView tvDataCadastro;
        TextView tvNomeAutenticador;
        TextView tvDataAutenticada;
        RelativeLayout bodyAutenticacao;
        ImageView btnAprovar;
        ImageView btnNeggar;
        ImageView imgAutenticado;


        public AlunoHolder(View v) {
            super(v);
            tvNome = v.findViewById(R.id.tv_nome);
            imgUser = v.findViewById(R.id.img_user);
            tvCpf = v.findViewById(R.id.tv_cpf);
            tvMatricula = v.findViewById(R.id.tv_matricula);
            tvDataCadastro = v.findViewById(R.id.tv_data_cadastro);
            btnAprovar = v.findViewById(R.id.btn_aprovar);
            btnNeggar = v.findViewById(R.id.btn_negar);
            tvNomeAutenticador = v.findViewById(R.id.tv_nome_autenticador);
            tvDataAutenticada = v.findViewById(R.id.tv_data_autenticada);
            bodyAutenticacao = v.findViewById(R.id.rr_body_autenticador);
            imgAutenticado = v.findViewById(R.id.img_autenticado);
        }
    }

}
