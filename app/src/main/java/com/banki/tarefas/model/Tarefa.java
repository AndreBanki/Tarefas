package com.banki.tarefas.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Tarefa implements Serializable {

    private Locale PT_BR = new Locale("pt","BR");

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

    public String getDueDateAsString() {
        String data = "";
        if (dueDate != 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(dueDate);

            SimpleDateFormat dateFormat;
            Calendar hoje = Calendar.getInstance();
            if (cal.get(Calendar.YEAR) == hoje.get(Calendar.YEAR))
                dateFormat = new SimpleDateFormat("dd/MMM", PT_BR);
            else
                dateFormat = new SimpleDateFormat("dd/MMM/yy", PT_BR);
            data = dateFormat.format(cal.getTime());
        }
        return data;
    }

    public String getReminderAsString() {
        String hora = "";
        if (dueDate != 0 && reminder) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(dueDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", PT_BR);
            hora = timeFormat.format(cal.getTime());
        }
        return hora;
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

    public void setDueDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dueDate = cal.getTimeInMillis();
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
