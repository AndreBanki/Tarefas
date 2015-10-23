package com.banki.tarefas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.banki.tarefas.model.Tarefa;

import java.util.ArrayList;

public class TarefaDAO {

    private TarefaSQL dbHelper;
    private SQLiteDatabase db;

    public TarefaDAO(Context ctx) {
        dbHelper = new TarefaSQL(ctx);
        db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Tarefa> listaTarefas() {
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        String[] colunas = new String[]{
                TarefaSQL._ID,
                TarefaSQL.DESCRICAO,
                TarefaSQL.COMPLETED,
                TarefaSQL.DUEDATE,
                TarefaSQL.REMINDER,
                TarefaSQL.REMINDERMINUTES
        };
        Cursor c = db.query(TarefaSQL.TABELA, colunas, null, null, null, null, null);

        c.moveToFirst();
        int nTarefas = c.getCount();
        for (int i = 0; i < nTarefas; i++) {
            Tarefa tarefa = new Tarefa();
            tarefa.setId(c.getInt(0));
            tarefa.setDescricao(c.getString(1));
            tarefa.setCompleted(c.getInt(2));
            tarefa.setDueDate(c.getLong(3));
            tarefa.setReminder(c.getInt(4));
            tarefa.setReminderMinutes(c.getInt(5));

            tarefas.add(tarefa);
            c.moveToNext();
        }
        return tarefas;
    }

    public int inserir(Tarefa tarefa) {
        ContentValues valores = fieldValuesFrom(tarefa);
        return (int)db.insert(TarefaSQL.TABELA, null, valores);
    }

    public void atualizar(Tarefa tarefa) {
        ContentValues valores = fieldValuesFrom(tarefa);
        String where = TarefaSQL._ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(tarefa.getId()) };

        db.update(TarefaSQL.TABELA, valores, where, whereArgs);
    }

    @NonNull
    private ContentValues fieldValuesFrom(Tarefa tarefa) {
        ContentValues valores = new ContentValues();
        valores.put(TarefaSQL.COMPLETED, tarefa.isCompleted());
        valores.put(TarefaSQL.DESCRICAO, tarefa.getDescricao());
        valores.put(TarefaSQL.DUEDATE, tarefa.getDueDate());
        valores.put(TarefaSQL.REMINDER, tarefa.hasReminder());
        valores.put(TarefaSQL.REMINDERMINUTES, tarefa.getReminderMinutes());
        return valores;
    }

    public void apagar(Tarefa tarefa){
        if (tarefa.getId() != 0) {
            String whereClause = TarefaSQL._ID + " = ?";
            String[] whereValues = new String[]{String.valueOf(tarefa.getId())};
            db.delete(TarefaSQL.TABELA, whereClause, whereValues);
        }
    }

    public void fechar() {
        db.close();
        dbHelper.close();
    }

    public void limparTudo() {
        dbHelper.onUpgrade(db, 0, 0);
    }
}
