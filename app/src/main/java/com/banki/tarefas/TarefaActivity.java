package com.banki.tarefas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.banki.tarefas.model.Tarefa;

import java.util.Calendar;

public class TarefaActivity extends AppCompatActivity {

    private EditText editDescricao;
    private TextView textData;
    protected Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        tarefa = (Tarefa)getIntent().getSerializableExtra("tarefa");

        editDescricao = (EditText)findViewById(R.id.editDescricao);

        textData = (TextView)findViewById(R.id.textData);
        preencheData();
        textData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DatePickerFragment date = new DatePickerFragment(tarefa);
                date.setCallBack(ondate);
                date.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        Button btnOK = (Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String descricao = editDescricao.getText().toString();
                tarefa.setDescricao(descricao);

                Intent intent = new Intent();
                intent.putExtra("tarefa", tarefa);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void preencheData() {
        if (tarefa.getDueDate() == 0)
            textData.setText("Definir data limite");
        else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(tarefa.getDueDate());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            textData.setText("Data limite: " + String.valueOf(day) +
                             "/" + String.valueOf(month) +
                             "/" + String.valueOf(year));
        }
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tarefa.setDueDate(cal.getTimeInMillis());
            preencheData();
        }
    };

    public class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener ondateSet;

        private Tarefa tarefa;

        public DatePickerFragment(Tarefa tarefa) {
            this.tarefa = tarefa;
        }

        public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
            ondateSet = ondate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            if (tarefa.getDueDate() != 0)
                cal.setTimeInMillis(tarefa.getDueDate());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        }
    }


}
