package com.example.calendartrainingevents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_EVENT = 1;

    private RecyclerView rvEvents;
    private TextView tvSelectedDate, tvEventCount;
    private CalendarView calendarView;
    private EventAdapter eventAdapter;
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> filteredEvents = new ArrayList<>();
    private long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инициализация
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvEventCount = findViewById(R.id.tvEventCount);
        calendarView = findViewById(R.id.calendarView);
        rvEvents = findViewById(R.id.rvEvents);

        // Настройка RecyclerView
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        rvEvents.setAdapter(eventAdapter);

        // Установка текущей даты
        selectedDateMillis = System.currentTimeMillis();
        updateSelectedDateText();

        // Календарь
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            selectedDateMillis = cal.getTimeInMillis();
            updateSelectedDateText();
            filterEventsByDate();
        });

        // FAB для добавления события
        FloatingActionButton fab = findViewById(R.id.fabAddEvent);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
            startActivityForResult(intent, REQUEST_ADD_EVENT);
        });

        // Обработка клика на событие
        eventAdapter.setOnItemClickListener(event -> {
            Intent intent = new Intent(MainActivity.this, EventDetailActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        });

        // Тестовое событие для примера
        addSampleEvent();
    }

    private void addSampleEvent() {
        Event sampleEvent = new Event();
        sampleEvent.setId(1);
        sampleEvent.setTitle("Лекция по программированию");
        sampleEvent.setDateTime("25.05.2026 10:00");
        sampleEvent.setLocation("Аудитория 210");
        sampleEvent.setOrganizer("Проф. Иванов И.И.");
        sampleEvent.setDescription("Лекция по основам программирования");
        sampleEvent.setDuration(90);
        allEvents.add(sampleEvent);
        filterEventsByDate();
    }

    private void updateSelectedDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault());
        tvSelectedDate.setText(sdf.format(new Date(selectedDateMillis)));
    }

    private void filterEventsByDate() {
        filteredEvents.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String selectedDateStr = sdf.format(new Date(selectedDateMillis));

        for (Event event : allEvents) {
            if (event.getDateTime().startsWith(selectedDateStr)) {
                filteredEvents.add(event);
            }
        }

        eventAdapter.setEvents(filteredEvents);
        tvEventCount.setText(filteredEvents.size() + " событий");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_EVENT && resultCode == RESULT_OK && data != null) {
            Event newEvent = (Event) data.getSerializableExtra("new_event");
            if (newEvent != null) {
                allEvents.add(newEvent);
                filterEventsByDate();
            }
        }
    }
}