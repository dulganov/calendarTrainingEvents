package com.example.calendartrainingevents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
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
    private static final Locale RU_LOCALE = new Locale("ru");
    private static final SimpleDateFormat EVENT_DATE_FORMAT =
            new SimpleDateFormat("dd.MM.yyyy", RU_LOCALE);

    private RecyclerView rvEvents;
    private TextView tvSelectedDate;
    private TextView tvEventCount;
    private CalendarView calendarView;
    private EventAdapter eventAdapter;
    private final List<Event> allEvents = new ArrayList<>();
    private final List<Event> filteredEvents = new ArrayList<>();
    private long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvEventCount = findViewById(R.id.tvEventCount);
        calendarView = findViewById(R.id.calendarView);
        rvEvents = findViewById(R.id.rvEvents);
        Button btnToday = findViewById(R.id.btnToday);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        rvEvents.setAdapter(eventAdapter);

        selectedDateMillis = System.currentTimeMillis();
        calendarView.setDate(selectedDateMillis, false, true);
        updateSelectedDateText();

        addSampleEventForToday();
        filterEventsByDate();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            selectedDateMillis = calendar.getTimeInMillis();
            updateSelectedDateText();
            filterEventsByDate();
        });

        btnToday.setOnClickListener(v -> {
            selectedDateMillis = System.currentTimeMillis();
            calendarView.setDate(selectedDateMillis, true, true);
            updateSelectedDateText();
            filterEventsByDate();
        });

        FloatingActionButton fab = findViewById(R.id.fabAddEvent);
        fab.setOnClickListener(v -> openAddEventScreen());

        eventAdapter.setOnItemClickListener(event -> {
            Intent intent = new Intent(MainActivity.this, EventDetailActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        });
    }

    private void openAddEventScreen() {
        Intent intent = new Intent(MainActivity.this, AddEditEventActivity.class);
        intent.putExtra("selected_date_millis", selectedDateMillis);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    private void addSampleEventForToday() {
        Event sampleEvent = new Event();
        sampleEvent.setId(1);
        sampleEvent.setTitle("Лекция по программированию");
        sampleEvent.setDateTime(EVENT_DATE_FORMAT.format(new Date()) + " 10:00");
        sampleEvent.setLocation("Аудитория 210");
        sampleEvent.setOrganizer("Преподаватель Иванов И.И.");
        sampleEvent.setDescription("Занятие по основам программирования");
        sampleEvent.setDuration(90);
        sampleEvent.setColor(getColor(R.color.colorPrimary));
        allEvents.add(sampleEvent);
    }

    private void updateSelectedDateText() {
        SimpleDateFormat titleFormat = new SimpleDateFormat("dd MMMM yyyy, EEEE", RU_LOCALE);
        String selectedDate = EVENT_DATE_FORMAT.format(new Date(selectedDateMillis));
        String today = EVENT_DATE_FORMAT.format(new Date());

        if (selectedDate.equals(today)) {
            tvSelectedDate.setText("Сегодня, " + titleFormat.format(new Date(selectedDateMillis)));
        } else {
            tvSelectedDate.setText(titleFormat.format(new Date(selectedDateMillis)));
        }
    }

    private void filterEventsByDate() {
        filteredEvents.clear();

        String selectedDate = EVENT_DATE_FORMAT.format(new Date(selectedDateMillis));
        for (Event event : allEvents) {
            if (event.getDateTime() != null && event.getDateTime().startsWith(selectedDate)) {
                filteredEvents.add(event);
            }
        }

        eventAdapter.setEvents(filteredEvents);
        tvEventCount.setText(getEventCountText(filteredEvents.size()));
    }

    private String getEventCountText(int count) {
        if (count % 10 == 1 && count % 100 != 11) {
            return count + " событие";
        }
        if (count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 >= 20)) {
            return count + " события";
        }
        return count + " событий";
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_event) {
            openAddEventScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
