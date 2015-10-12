package com.banki.tarefas;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

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

        trataAberturaViaNotificacao();
    }

    private void trataAberturaViaNotificacao() {
        Tarefa tarefaNotificada = (Tarefa)getIntent().getSerializableExtra("tarefa");
        if (tarefaNotificada != null) {
            Tarefa tarefaEditar = tarefasList.find(tarefaNotificada);
            if (tarefaEditar == null)
                Toast.makeText(this, "Tarefa não encontrada. Provavelmente, já foi concluída.",
                               Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                intent.putExtra("tarefa", tarefaEditar);
                startActivityForResult(intent, REQUEST_TAREFA);
            }
        }
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
                if (tarefa.isCompleted()) {
                    tarefasList.apagar(tarefa);
                    criaSnackBarOpcaoDesfazer(tarefa);
                }
                else {
                    tarefasList.salvar(tarefa); // atribui o ID se estiver criando
                    defineAlarme(tarefa);
                }
            }
        }
    }

    private void defineAlarme(Tarefa tarefa) {
        if (tarefa.getDueDate() != 0 && tarefa.hasReminder()) {
            PendingIntent pendingIt = criaIntentQueSeraExecutadoPeloAlarme(tarefa);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, tarefa.getDueDate(), pendingIt);
        }
    }

    private PendingIntent criaIntentQueSeraExecutadoPeloAlarme(Tarefa tarefa) {
        Intent intent = new Intent("ALERTA_TAREFA");
        intent.putExtra("tarefa", tarefa);

        final int requestCode = 0;
        return PendingIntent.getBroadcast(
                MainActivity.this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
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
        tarefasList.loadData();
    }

    private TarefaAdapter.TarefaOnClickListener onClickTarefa(){
        return new TarefaAdapter.TarefaOnClickListener(){
            @Override
            public void onClickTarefa(View view, int index){
                Tarefa tarefa = tarefasList.get(index);
                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                intent.putExtra("tarefa", tarefa);
                startActivityForResult(intent, REQUEST_TAREFA);
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
                        tarefasList.salvar(tarefa);
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
