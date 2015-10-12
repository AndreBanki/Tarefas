package com.banki.tarefas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.banki.tarefas.model.Tarefa;

public class TarefaActivity extends Activity {

    private EditText editDescricao;
    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        tarefa = (Tarefa)getIntent().getSerializableExtra("tarefa");

        editDescricao = (EditText)findViewById(R.id.editDescricao);

        Button btnOK = (Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String descricao = editDescricao.getText().toString();
                tarefa.setDescricao(descricao);

                Intent intent = new Intent();
                intent.putExtra("tarefa",tarefa);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
