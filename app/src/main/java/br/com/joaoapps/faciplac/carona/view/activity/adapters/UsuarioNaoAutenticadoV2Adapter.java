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

import com.example.joaov.caronasolidaria.R;
import com.github.abdularis.civ.CircleImageView;
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

public class UsuarioNaoAutenticadoV2Adapter extends RecyclerView.Adapter<UsuarioNaoAutenticadoV2Adapter.AlunoHolder> {

    private List<Usuario> usuarios;
    private AppCompatActivity context;

    public UsuarioNaoAutenticadoV2Adapter(List<Usuario> usuarios, AppCompatActivity context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public AlunoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alunos_v2, parent, false);
        return new UsuarioNaoAutenticadoV2Adapter.AlunoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoHolder holder, final int position) {
        final Usuario usuario = usuarios.get(position);

        holder.tvNome.setText(usuario.getNome());
        if (usuario.getUrlFoto() != null && !usuario.getUrlFoto().isEmpty()) {
            Picasso.with(context)
                    .load(usuario.getUrlFoto())
                    .placeholder(R.drawable.icon_carona)
                    .error(R.drawable.icon_user_default)
                    .into(holder.imgProfile);
        }

        holder.tvNameAdm.setText(usuario.getAutenticado().getNomeAutenticador());


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
        TextView tvNameAdm;
        CircleImageView imgProfile;
        ViewGroup llBody;
        ImageView btnAprovar;
        ImageView btnNeggar;


        public AlunoHolder(View v) {
            super(v);
            tvNome = v.findViewById(R.id.tv_name);
            tvNameAdm = v.findViewById(R.id.tv_name_adm);
            btnAprovar = v.findViewById(R.id.btn_aprovar);
            btnNeggar = v.findViewById(R.id.btn_negar);
            llBody = v.findViewById(R.id.ll_body);
            imgProfile = v.findViewById(R.id.img_profile);
            btnNeggar = v.findViewById(R.id.btn_negar);
        }
    }

}
