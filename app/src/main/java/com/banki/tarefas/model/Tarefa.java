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

    public Tarefa(String descricao) {
        this.id = 0;
        this.completed = false;
        this.descricao = descricao;
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
        if (completed == 0)
            this.completed = false;
        else
            this.completed = true;
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

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public void setReminder(int reminder) {
        if (reminder == 0)
            this.reminder = false;
        else
            this.reminder = true;
    }

    public int getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(int reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }
}
