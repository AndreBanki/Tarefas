package com.banki.tarefas;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.banki.tarefas.model.Tarefa;

import java.util.Calendar;

public class TarefaActivity extends AppCompatActivity {

    private TextView textData;
    private TextView textHora;
    private CheckBox check;
    private EditText editDescricao;
    protected Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        tarefa = (Tarefa)getIntent().getSerializableExtra("tarefa");

        criaEdit();
        criaCheck();
        criaDatePicker();
        criaTimePicker();
        criaBtnSalvar();
        criaBtnCancelar();
    }

    private void criaEdit() {
        editDescricao = (EditText) findViewById(R.id.editDescricao);
        if (tarefa.getId() != 0)
            editDescricao.setText(tarefa.getDescricao());
    }

    private void criaCheck() {
        check = (CheckBox)findViewById(R.id.checkTarefa);
        if (tarefa.getId() == 0)
            check.setEnabled(false);
    }

    private void criaBtnSalvar() {
        Button btnOK = (Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String descricao = editDescricao.getText().toString();
                Intent returnIntent = new Intent();
                if (descricao.isEmpty())
                    setResult(RESULT_CANCELED, returnIntent);
                else {
                    tarefa.setDescricao(descricao);
                    tarefa.setCompleted(check.isChecked());

                    returnIntent.putExtra("tarefa", tarefa);
                    setResult(RESULT_OK, returnIntent);
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

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(tarefa.getDueDate());
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE,minute);

            tarefa.setReminder(true);
            tarefa.setDueDate(cal.getTimeInMillis());
            preencheHora();
        }
    };

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment {

        private Tarefa tarefa;

        public DatePickerFragment(Tarefa tarefa) {
            this.tarefa = tarefa;
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
            return new DatePickerWithNeutral(getActivity(), year, month, day);
        }
    }

    public class DatePickerWithNeutral extends DatePickerDialog {

        public DatePickerWithNeutral(Context context, int year, int monthOfYear, int dayOfMonth) {
            super(context, 0, null, year, monthOfYear, dayOfMonth);

            setButton(BUTTON_POSITIVE, ("Definir"), this);
            setButton(BUTTON_NEUTRAL, ("Limpar"), this);
            setButton(BUTTON_NEGATIVE, ("Cancelar"), this);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getButton(BUTTON_NEUTRAL).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tarefa.setDueDate(0);
                            dismiss();
                            preencheData();
                            preencheHora();
                        }
                    });
            getButton(BUTTON_POSITIVE).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tarefa.setDueDate(getDatePicker().getYear(),
                                              getDatePicker().getMonth(),
                                              getDatePicker().getDayOfMonth());
                            dismiss();
                            preencheData();
                            preencheHora();
                        }
                    });
            getButton(BUTTON_NEGATIVE).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });
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

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            final boolean is24HourView = true;
            return new TimePickerDialog(getActivity(), ontimeSet, hour, minute, is24HourView);
        }
    }

}
