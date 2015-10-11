package com.banki.tarefas.model;

import java.util.Date;

public class Tarefa {

    public static String _ID = "_id";
    public static String COMPLETED = "completed";
    public static String DESCRICAO = "descricao";
    public static String DUEDATE = "duedate";
    public static String REMINDER = "reminder";
    public static String REMINDERMINUTES = "reminderminutes";

    private int id;
    private boolean completed;
    private String descricao;
    private long dueDate;
    private boolean reminder;
    private int reminderMinutes;

    public Tarefa() {
        this.id = 0;
        this.completed = false;
        this.descricao = "";
        this.dueDate = 0;
        this.reminder = false;
        reminderMinutes = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed != 0;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public boolean hasReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder != 0;
    }

    public int getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(int reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }
}
