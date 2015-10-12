package com.banki.tarefas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.banki.tarefas.adapter.TarefaAdapter;
import com.banki.tarefas.controller.TarefaListController;
import com.banki.tarefas.model.Tarefa;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAREFA = 0;
    TarefaListController tarefasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initAddButton();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tarefasList.loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAREFA) {
            if (resultCode == RESULT_OK) {
                Tarefa tarefa = (Tarefa)data.getSerializableExtra("tarefa");
                tarefasList.inserir(tarefa);
            }
        }
    }

    private void initAddButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tarefa t = new Tarefa();
                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                intent.putExtra("tarefa", t);
                startActivityForResult(intent, REQUEST_TAREFA);
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        tarefasList = new TarefaListController(this, recyclerView, onClickTarefa());
    }

    private TarefaAdapter.TarefaOnClickListener onClickTarefa(){
        return new TarefaAdapter.TarefaOnClickListener(){
            @Override
            public void onClickTarefa(View view, int index){
                Tarefa tarefa = tarefasList.get(index);
                tarefasList.apagar(tarefa);
                criaSnackBarOpcaoDesfazer(tarefa);
            }
        };
    }

    private void criaSnackBarOpcaoDesfazer(final Tarefa tarefa) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Tarefa concluída!", Snackbar.LENGTH_INDEFINITE)
                .setAction("DESFAZER", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tarefa.setId(0);
                        tarefasList.inserir(tarefa);
                    }
                });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sample)
            tarefasList.createSampleData();
        else if (id == R.id.action_destroy)
            tarefasList.resetData();

        return super.onOptionsItemSelected(item);
    }
}
