package com.example.calendartrainingevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etDate, etTime, etDuration, etLocation, etOrganizer, etDescription;
    private Spinner spinnerColor;
    private TextView tvColorPreview;
    private Button btnSave, btnCancel;

    private String selectedColor = "#2196F3"; // цвет по умолчанию

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        // Настройка Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Инициализация полей
        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etDuration = findViewById(R.id.etDuration);
        etLocation = findViewById(R.id.etLocation);
        etOrganizer = findViewById(R.id.etOrganizer);
        etDescription = findViewById(R.id.etDescription);
        spinnerColor = findViewById(R.id.spinnerColor);
        tvColorPreview = findViewById(R.id.tvColorPreview);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        etTitle.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        etLocation.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etOrganizer.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etDescription.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        // Настройка Spinner для выбора цвета
        setupColorSpinner();

        // Обработчик выбора даты
        etDate.setOnClickListener(v -> showDatePicker());
        etDate.setFocusable(false);
        etDate.setClickable(true);

        // Обработчик выбора времени
        etTime.setOnClickListener(v -> showTimePicker());
        etTime.setFocusable(false);
        etTime.setClickable(true);

        // Кнопка Сохранить
        btnSave.setOnClickListener(v -> saveEvent());

        // Кнопка Отмена
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupColorSpinner() {
        String[] colors = {"Синий", "Зеленый", "Красный", "Оранжевый", "Фиолетовый"};
        String[] colorValues = {"#2196F3", "#4CAF50", "#F44336", "#FF9800", "#9C27B0"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapter);

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColor = colorValues[position];
                tvColorPreview.setBackgroundColor(android.graphics.Color.parseColor(selectedColor));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedColor = "#2196F3";
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "." + (month1 + 1) + "." + year1;
                    etDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    etTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveEvent() {
        // Получаем данные из полей
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String date = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String time = etTime.getText() != null ? etTime.getText().toString().trim() : "";
        String durationStr = etDuration.getText() != null ? etDuration.getText().toString().trim() : "60";
        String location = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
        String organizer = etOrganizer.getText() != null ? etOrganizer.getText().toString().trim() : "";
        String description = etDescription.getText() != null ? etDescription.getText().toString().trim() : "";

        int duration = 60;
        try {
            duration = Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            duration = 60;
        }

        // Проверка на заполнение обязательных полей
        if (title.isEmpty()) {
            etTitle.setError("Введите название события");
            return;
        }

        if (date.isEmpty() || date.equals("Выберите дату")) {
            etDate.setError("Выберите дату");
            return;
        }

        if (time.isEmpty() || time.equals("Выберите время")) {
            etTime.setError("Выберите время");
            return;
        }

        // Создаем новое событие
        Event newEvent = new Event();
        newEvent.setId((int) System.currentTimeMillis());
        newEvent.setTitle(title);
        newEvent.setDateTime(date + " " + time);
        newEvent.setLocation(location.isEmpty() ? "Не указано" : location);
        newEvent.setOrganizer(organizer.isEmpty() ? "Не указан" : organizer);
        newEvent.setDescription(description.isEmpty() ? "Нет описания" : description);
        newEvent.setDuration(duration);
        newEvent.setColor(android.graphics.Color.parseColor(selectedColor));

        // Возвращаем результат в MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_event", newEvent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}