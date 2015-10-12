package com.banki.tarefas.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.banki.tarefas.MainActivity;
import com.banki.tarefas.R;
import com.banki.tarefas.model.Tarefa;

public class TarefaNotifier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Tarefa tarefa = (Tarefa) intent.getSerializableExtra("tarefa");

        PendingIntent pendingIt = criaIntentQueSeraDisparadoPelaNotificacao(context, tarefa);
        criaNotificacao(context, tarefa, pendingIt);
    }

    private PendingIntent criaIntentQueSeraDisparadoPelaNotificacao(Context context, Tarefa tarefa) {
        Intent forwardIt = new Intent(context, MainActivity.class);
        forwardIt.putExtra("tarefa", tarefa);

        final int requestCode = 0;
        return PendingIntent.getActivity(
                context,
                requestCode,
                forwardIt,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent criaIntentQueSeraDisparadoPelaNotificacao2(Context context, Tarefa tarefa) {
        Intent forwardIt = new Intent("ABRIR_TAREFA");
        forwardIt.putExtra("tarefa", tarefa);

        final int requestCode = 0;
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                forwardIt,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void criaNotificacao(Context context, Tarefa tarefa, PendingIntent pendingIt) {
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                .setTicker("Nova tarefa")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(tarefa.getDescricao())
                .setContentText("Vence em " + tarefa.getDueDateAsString())
                .setContentIntent(pendingIt)
                .setWhen(tarefa.getDueDate())
                .setAutoCancel(true);

        NotificationManager nManager;
        nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(R.string.app_name, notif.build());
    }
}