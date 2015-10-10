package com.banki.tarefas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.banki.tarefas.model.Tarefa;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static String TABELA = "tarefa";

    private static String nomeBanco = "tarefas";
    private static int versaoBanco = 2;

    public SQLiteHelper(Context context) {
        super(context, nomeBanco, null, versaoBanco);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String scriptSQLCreate = "create table " + TABELA + "(" +
                Tarefa._ID + " integer primary key autoincrement," +
                Tarefa.DESCRICAO + " text not null," +
                Tarefa.COMPLETED + " completed integer," +
                Tarefa.DUEDATE + " integer," +
                Tarefa.REMINDER+ " integer," +
                Tarefa.REMINDERMINUTES + " integer" +
                ");";
        db.execSQL(scriptSQLCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        deleteAllData(db);
        onCreate(db);
    }

    private void deleteAllData(SQLiteDatabase db) {
        String scriptSQLDelete = "drop table if exists " + TABELA;
        db.execSQL(scriptSQLDelete);
    }
}
