package com.example.calendartrainingevents;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tvDetailTitle, tvDetailDateTime, tvDetailLocation, tvDetailOrganizer, tvDetailDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Настройка Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Инициализация
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailOrganizer = findViewById(R.id.tvDetailOrganizer);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);

        // Получаем событие из Intent
        Event event = (Event) getIntent().getSerializableExtra("event");

        if (event != null) {
            tvDetailTitle.setText(event.getTitle());
            tvDetailDateTime.setText("📅 " + event.getDateTime());
            tvDetailLocation.setText("📍 " + event.getLocation());
            tvDetailOrganizer.setText("💻 " + event.getOrganizer());
            tvDetailDescription.setText(event.getDescription());
        }
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