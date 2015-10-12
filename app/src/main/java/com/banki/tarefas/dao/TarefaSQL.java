package com.banki.tarefas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.banki.tarefas.model.Tarefa;

public class TarefaSQL extends SQLiteOpenHelper {

    public static String TABELA = "tarefa";

    public static String _ID = "_id";
    public static String COMPLETED = "completed";
    public static String DESCRICAO = "descricao";
    public static String DUEDATE = "duedate";
    public static String REMINDER = "reminder";
    public static String REMINDERMINUTES = "reminderminutes";

    private static String nomeBanco = "tarefas";
    private static int versaoBanco = 2;

    public TarefaSQL(Context context) {
        super(context, nomeBanco, null, versaoBanco);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String scriptSQLCreate = "create table " + TABELA + "(" +
                _ID + " integer primary key autoincrement," +
                DESCRICAO + " text not null," +
                COMPLETED + " completed integer," +
                DUEDATE + " integer," +
                REMINDER+ " integer," +
                REMINDERMINUTES + " integer" +
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
