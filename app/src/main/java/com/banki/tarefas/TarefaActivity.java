package com.banki.tarefas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.banki.tarefas.model.Tarefa;

import java.util.Calendar;
import java.util.TimeZone;

public class TarefaActivity extends AppCompatActivity {

    private TextView textData;
    private TextView textHora;
    protected Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        tarefa = (Tarefa)getIntent().getSerializableExtra("tarefa");

        criaDatePicker();
        criaTimePicker();
        criaBtnSalvar();
        criaBtnCancelar();
    }

    private void criaBtnSalvar() {
        Button btnOK = (Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText editDescricao = (EditText) findViewById(R.id.editDescricao);
                String descricao = editDescricao.getText().toString();
                Intent intent = new Intent();
                if (descricao.isEmpty())
                    setResult(RESULT_CANCELED, intent);
                else {
                    tarefa.setDescricao(descricao);
                    intent.putExtra("tarefa", tarefa);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    private void criaBtnCancelar() {
        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void criaDatePicker() {
        textData = (TextView)findViewById(R.id.textData);
        preencheData();
        textData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DatePickerFragment date = new DatePickerFragment(tarefa);
                date.setCallBack(dateSetListener);
                date.show(getSupportFragmentManager(), "Date Picker");
            }
        });
    }

    private void criaTimePicker() {
        textHora = (TextView)findViewById(R.id.textHora);
        preencheHora();
        textHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TimePickerFragment time = new TimePickerFragment(tarefa);
                time.setCallBack(timeSetListener);
                time.show(getSupportFragmentManager(), "Time Picker");
            }
        });
    }

    private void preencheData() {
        if (tarefa.getDueDate() == 0)
            textData.setText("Definir data limite");
        else
            textData.setText("Data limite: " + tarefa.getDueDateAsString());
    }

    private void preencheHora() {
        if (tarefa.getDueDate() == 0)
            textHora.setVisibility(View.INVISIBLE);
        else {
            textHora.setVisibility(View.VISIBLE);
            if (!tarefa.hasReminder())
                textHora.setText("Definir alerta");
            else
                textHora.setText("Alerta: " + tarefa.getReminderAsString());
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tarefa.setDueDate(year, monthOfYear, dayOfMonth);
            preencheData();
            preencheHora();
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(tarefa.getDueDate());
            cal.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE,minute);

            tarefa.setReminder(true);
            tarefa.setDueDate(cal.getTimeInMillis());
            preencheHora();
        }
    };

    @SuppressLint("ValidFragment")
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
        @NonNull
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

    public class TimePickerFragment extends DialogFragment {

        TimePickerDialog.OnTimeSetListener ontimeSet;
        private Tarefa tarefa;

        public TimePickerFragment(Tarefa tarefa) {
            this.tarefa = tarefa;
        }

        public void setCallBack(TimePickerDialog.OnTimeSetListener ontimeSet) {
            this.ontimeSet = ontimeSet;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            if (tarefa.getDueDate() != 0)
                cal.setTimeInMillis(tarefa.getDueDate());

            cal.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            final boolean is24HourView = true;
            return new TimePickerDialog(getActivity(), ontimeSet, hour, minute, is24HourView);
        }
    }

}
