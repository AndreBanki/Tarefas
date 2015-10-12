package com.banki.tarefas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.banki.tarefas.R;
import com.banki.tarefas.model.Tarefa;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefasViewHolder> {

    private final ArrayList<Tarefa> tarefas;
    private final Context context;
    private TarefaOnClickListener tarefaOnClickListener;

    public TarefaAdapter(Context context, ArrayList<Tarefa> tarefas, TarefaOnClickListener tarefaOnClickListener){
        this.context = context;
        this.tarefas = tarefas;
        this.tarefaOnClickListener = tarefaOnClickListener;
    }

    @Override
    public int getItemCount(){
        return this.tarefas != null ? this.tarefas.size() : 0;
    }

    @Override
    public TarefasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tarefa, viewGroup, false);
        return new TarefasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TarefasViewHolder holder, final int position){
        Tarefa tarefa = tarefas.get(position);
        holder.firstLine.setText(tarefa.getDescricao());

        String msgAlarme = "";
        if (tarefa.hasReminder())
            msgAlarme = "Avisar " + tarefa.getReminderMinutes() + " minutos antes";
        holder.secondLine.setText(msgAlarme);

        String data = "";
        String hora = "";
        if (tarefa.getDueDate() != 0) {
            Locale PT_BR = new Locale("pt","BR");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(tarefa.getDueDate());

            SimpleDateFormat dateFormat;
            Calendar hoje = Calendar.getInstance();
            if (cal.get(Calendar.YEAR) == hoje.get(Calendar.YEAR))
                dateFormat = new SimpleDateFormat("dd/MMM", PT_BR);
            else
                dateFormat = new SimpleDateFormat("dd/MMM/yy", PT_BR);
            data = dateFormat.format(cal.getTime());

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", PT_BR);
            hora = timeFormat.format(cal.getTime());
        }
        holder.data.setText(data);
        holder.hora.setText(hora);

        if (tarefaOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    tarefaOnClickListener.onClickTarefa(holder.itemView, position);
                }
            });
        }
    }

    public interface TarefaOnClickListener {
        void onClickTarefa(View view, int idx);
    }

    public static class TarefasViewHolder extends RecyclerView.ViewHolder {
        public TextView firstLine;
        public TextView secondLine;
        public TextView data;
        public TextView hora;
        CardView cardView;

        public TarefasViewHolder(View view){
            super(view);
            data = (TextView) view.findViewById(R.id.data);
            hora = (TextView) view.findViewById(R.id.hora);
            firstLine = (TextView) view.findViewById(R.id.firstLine);
            secondLine = (TextView) view.findViewById(R.id.secondLine);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}

