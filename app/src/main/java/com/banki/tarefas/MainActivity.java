package com.banki.tarefas;

import android.os.Bundle;
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
import com.banki.tarefas.dao.TarefaDAO;
import com.banki.tarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Tarefa> tarefas = new ArrayList<>();
    TarefaDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initAddButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dao = new TarefaDAO(this);
        loadData();
        initRecyclerView();
    }

    private void loadData() {
        tarefas = dao.listaTarefas();
    }

    private void initAddButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tarefa t1 = new Tarefa();
                t1.setDescricao("Tarefa adicionada");
                dao.inserir(t1);
                tarefas.add(t1);
                updateRecyclerView();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        recyclerView.setAdapter(new TarefaAdapter(this, tarefas, onClickTarefa()));
    }

    private TarefaAdapter.TarefaOnClickListener onClickTarefa(){
        return new TarefaAdapter.TarefaOnClickListener(){
            @Override
            public void onClickTarefa(View view, int index){
                Tarefa tarefa = tarefas.get(index);
                dao.apagar(tarefa);
                tarefas.remove(tarefa);
                updateRecyclerView();
            }
        };
    }

    private void createSampleData() {
        Tarefa t1 = new Tarefa();
        t1.setDescricao("Pagar cartão");
        Calendar cal = Calendar.getInstance();
        t1.setDueDate(cal.getTimeInMillis());
        t1.setReminder(true);
        t1.setReminderMinutes(10);
        dao.inserir(t1);
        tarefas.add(t1);

        Tarefa t2 = new Tarefa();
        t2.setDescricao("Renovar seguro do carro");
        dao.inserir(t2);
        tarefas.add(t2);

        updateRecyclerView();
    }

    private void resetData() {
        dao.limparTudo();
        tarefas = new ArrayList<>();
        createSampleData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_sample) {
            createSampleData();
        }
        else if (id == R.id.action_destroy) {
            resetData();
        }

        return super.onOptionsItemSelected(item);
    }
}
