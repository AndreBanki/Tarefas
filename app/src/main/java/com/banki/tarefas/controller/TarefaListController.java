package com.banki.tarefas.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.banki.tarefas.adapter.TarefaAdapter;
import com.banki.tarefas.dao.TarefaDAO;
import com.banki.tarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.Calendar;

public class TarefaListController {

    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<Tarefa> tarefas = new ArrayList<>();
    private TarefaAdapter.TarefaOnClickListener clickListener;
    TarefaDAO dao;

    public TarefaListController(Context ctx, RecyclerView recyclerView, TarefaAdapter.TarefaOnClickListener clickListener) {
        this.context = ctx;
        this.recyclerView = recyclerView;
        this.clickListener = clickListener;
        dao = new TarefaDAO(ctx);
    }

    private void updateRecyclerView() {
        recyclerView.setAdapter(new TarefaAdapter(context, tarefas, clickListener));
    }

    public void loadData() {
        tarefas = dao.listaTarefas();
        updateRecyclerView();
    }

    public Tarefa get(int index) {
        return tarefas.get(index);
    }

    public void inserir(Tarefa tarefa) {
        int id = dao.inserir(tarefa);
        tarefa.setId(id);
        tarefas.add(tarefa);
        updateRecyclerView();
    }

    public void apagar(Tarefa tarefa) {
        dao.apagar(tarefa);
        tarefas.remove(tarefa);
        updateRecyclerView();
    }

    public void resetData() {
        dao.limparTudo();
        tarefas = new ArrayList<>();
        updateRecyclerView();
    }

    public void createSampleData() {
        Tarefa t1 = new Tarefa();
        t1.setDescricao("Pagar cartão");
        Calendar cal = Calendar.getInstance();
        t1.setDueDate(cal.getTimeInMillis());
        t1.setReminder(true);
        t1.setReminderMinutes(10);
        inserir(t1);

        Tarefa t2 = new Tarefa();
        t2.setDescricao("Renovar seguro do carro");
        inserir(t2);
    }
}