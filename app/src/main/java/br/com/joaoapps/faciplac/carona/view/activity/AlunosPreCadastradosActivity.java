package br.com.joaoapps.faciplac.carona.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;

import com.example.joaov.caronasolidaria.R;

import java.util.ArrayList;
import java.util.List;

import br.com.joaoapps.faciplac.carona.model.Usuario;
import br.com.joaoapps.faciplac.carona.model.enums.Situacao;
import br.com.joaoapps.faciplac.carona.model.enums.Status;
import br.com.joaoapps.faciplac.carona.service.firebase.UsuarioFirebase;
import br.com.joaoapps.faciplac.carona.service.listeners.OnRefreshAlunos;
import br.com.joaoapps.faciplac.carona.view.activity.adapters.UsuarioNaoAutenticadoAdapter;
import br.com.joaoapps.faciplac.carona.view.activity.dialogs.BottomDialogFilter;
import br.com.joaoapps.faciplac.carona.view.activity.dialogs.listeners.OnEventDialogListener;
import br.com.joaoapps.faciplac.carona.view.utils.AlertUtils;

public class AlunosPreCadastradosActivity extends AppCompatActivity {
    private List<Usuario> usuarios = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UsuarioNaoAutenticadoAdapter pedidosAdapter;
    private String query = "";

    private ImageView menuFilter;
    private SearchView searchView;
    private BottomDialogFilter bottomDialogFilter;
    private List<Situacao> situacaos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alunos_pre_cadastrados);

        initToolbar();
        initRecycler();
        situacaos = new ArrayList<>();
        situacaos.add(Situacao.ESPERA);

        UsuarioFirebase.getAll(new OnRefreshAlunos() {
            @Override
            public void getAll(List<Usuario> usuarios) {
                AlunosPreCadastradosActivity.this.usuarios = usuarios;
                refreshList(filterUsers(situacaos));
            }

            @Override
            public void error(int code) {
                AlertUtils.showAlert("Falha ao buscar alunos", AlunosPreCadastradosActivity.this);
            }
        });
    }

    private void initToolbar() {
        searchView = (SearchView) findViewById(R.id.searc_view);
        menuFilter = (ImageView) findViewById(R.id.filter);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlunosPreCadastradosActivity.this.query = query;
                refreshList(filterUsers(situacaos));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                refreshList(filterUsers(situacaos));
                return true;
            }
        });

        bottomDialogFilter = new BottomDialogFilter(AlunosPreCadastradosActivity.this, new OnEventDialogListener() {
            @Override
            public void onDismiss(List<Situacao> situacaos) {
                AlunosPreCadastradosActivity.this.situacaos = situacaos;
                refreshList(filterUsers(situacaos));
            }

            @Override
            public void onOpen() {
            }

            @Override
            public void onChange() {

            }
        });
        menuFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialogFilter.show();
            }
        });
    }

    void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        pedidosAdapter = new UsuarioNaoAutenticadoAdapter(usuarios, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pedidosAdapter);
    }

    void refreshList(List<Usuario> usuarios) {
        pedidosAdapter = new UsuarioNaoAutenticadoAdapter((usuarios), this);
        recyclerView.setAdapter(pedidosAdapter);
    }

    private List<Usuario> filterUsers(List<Situacao> situacaos) {
        List<Usuario> alunos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.getStatus() == Status.ALUNO) {
                for (Situacao situacao : situacaos) {
                    if (u.getLastAutenticado().getSituacao() == situacao && containsQuerySearch(u)) {
                        alunos.add(u);
                    }
                }
            }
        }
        return alunos;
    }

    private boolean containsQuerySearch(Usuario usuario) {
        return (usuario.getNome().contains(query) || usuario.getCpf().contains(query));
    }
}
