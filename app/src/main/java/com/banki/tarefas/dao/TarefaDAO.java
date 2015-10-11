package com.banki.tarefas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.banki.tarefas.model.Tarefa;

import java.util.ArrayList;

public class TarefaDAO {

    private SQLiteHelper dbHelper;
    private SQLiteDatabase db;

    public TarefaDAO(Context ctx) {
        dbHelper = new SQLiteHelper(ctx);
        db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Tarefa> listaTarefas() {
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        String[] colunas = new String[]{
                Tarefa._ID,
                Tarefa.DESCRICAO,
                Tarefa.COMPLETED,
                Tarefa.DUEDATE,
                Tarefa.REMINDER,
                Tarefa.REMINDERMINUTES
        };
        Cursor c = db.query(SQLiteHelper.TABELA, colunas, null, null, null, null, null);

        c.moveToFirst();
        int nTarefas = c.getCount();
        for (int i = 0; i < nTarefas; i++) {
            Tarefa tarefa = new Tarefa();
            tarefa.setId(c.getInt(0));
            tarefa.setDescricao(c.getString(1));
            tarefa.setCompleted(c.getInt(2));
            tarefa.setDueDate(c.getInt(3));
            tarefa.setReminder(c.getInt(4));
            tarefa.setReminderMinutes(c.getInt(5));

            tarefas.add(tarefa);
            c.moveToNext();
        }

        return tarefas;
    }

    public void inserir(Tarefa tarefa) {
        ContentValues valores = new ContentValues();
        valores.put(Tarefa.COMPLETED, tarefa.isCompleted());
        valores.put(Tarefa.DESCRICAO, tarefa.getDescricao());
        valores.put(Tarefa.DUEDATE, tarefa.getDueDate());
        valores.put(Tarefa.REMINDER, tarefa.hasReminder());
        valores.put(Tarefa.REMINDERMINUTES, tarefa.getReminderMinutes());
        db.insert(SQLiteHelper.TABELA, null, valores);
    }

    public void apagar(Tarefa tarefa){
        if (tarefa.getId() != 0) {
            String whereClause = Tarefa._ID + " = ?";
            String[] whereValues = new String[]{String.valueOf(tarefa.getId())};
            db.delete(SQLiteHelper.TABELA, whereClause, whereValues);
        }
    }

    public void fechar() {
        db.close();
    }

    public void limparTudo() {
        dbHelper.onUpgrade(db, 0, 0);
    }
}
