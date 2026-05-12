package com.example.calendartrainingevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    private static final Locale RU_LOCALE = new Locale("ru");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", RU_LOCALE);

    private TextInputEditText etTitle;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputEditText etDuration;
    private TextInputEditText etLocation;
    private TextInputEditText etOrganizer;
    private TextInputEditText etDescription;
    private Spinner spinnerColor;
    private TextView tvColorPreview;

    private String selectedColor = "#2196F3";
    private final Calendar selectedCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etDuration = findViewById(R.id.etDuration);
        etLocation = findViewById(R.id.etLocation);
        etOrganizer = findViewById(R.id.etOrganizer);
        etDescription = findViewById(R.id.etDescription);
        spinnerColor = findViewById(R.id.spinnerColor);
        tvColorPreview = findViewById(R.id.tvColorPreview);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        etTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        etLocation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etOrganizer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etDescription.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        long selectedDateMillis = getIntent().getLongExtra("selected_date_millis", System.currentTimeMillis());
        selectedCalendar.setTimeInMillis(selectedDateMillis);
        etDate.setText(DATE_FORMAT.format(new Date(selectedDateMillis)));
        etTime.setText(new SimpleDateFormat("HH:mm", RU_LOCALE).format(new Date()));

        setupColorSpinner();

        etDate.setOnClickListener(v -> showDatePicker());
        etDate.setFocusable(false);
        etDate.setClickable(true);

        etTime.setOnClickListener(v -> showTimePicker());
        etTime.setFocusable(false);
        etTime.setClickable(true);

        btnSave.setOnClickListener(v -> saveEvent());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupColorSpinner() {
        String[] colors = {"Синий", "Зелёный", "Красный", "Оранжевый", "Фиолетовый"};
        String[] colorValues = {"#2196F3", "#4CAF50", "#F44336", "#FF9800", "#9C27B0"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapter);

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColor = colorValues[position];
                tvColorPreview.setBackgroundColor(Color.parseColor(selectedColor));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedColor = "#2196F3";
                tvColorPreview.setBackgroundColor(Color.parseColor(selectedColor));
            }
        });
    }

    private void showDatePicker() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, dayOfMonth) -> {
                    selectedCalendar.set(selectedYear, selectedMonth, dayOfMonth);
                    etDate.setText(DATE_FORMAT.format(selectedCalendar.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, selectedMinute) -> {
                    String time = String.format(RU_LOCALE, "%02d:%02d", hourOfDay, selectedMinute);
                    etTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveEvent() {
        String title = getText(etTitle);
        String date = getText(etDate);
        String time = getText(etTime);
        String durationText = getText(etDuration);
        String location = getText(etLocation);
        String organizer = getText(etOrganizer);
        String description = getText(etDescription);

        if (title.isEmpty()) {
            etTitle.setError("Введите название события");
            return;
        }

        if (date.isEmpty()) {
            etDate.setError("Выберите дату");
            return;
        }

        if (time.isEmpty()) {
            etTime.setError("Выберите время");
            return;
        }

        int duration = 60;
        try {
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException ignored) {
            duration = 60;
        }

        if (duration <= 0) {
            etDuration.setError("Укажите длительность больше 0");
            return;
        }

        Event newEvent = new Event();
        newEvent.setId((int) System.currentTimeMillis());
        newEvent.setTitle(title);
        newEvent.setDateTime(date + " " + time);
        newEvent.setLocation(location.isEmpty() ? "Не указано" : location);
        newEvent.setOrganizer(organizer.isEmpty() ? "Не указан" : organizer);
        newEvent.setDescription(description.isEmpty() ? "Нет описания" : description);
        newEvent.setDuration(duration);
        newEvent.setColor(Color.parseColor(selectedColor));

        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_event", newEvent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
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
