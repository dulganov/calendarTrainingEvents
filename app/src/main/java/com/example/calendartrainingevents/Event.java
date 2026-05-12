package com.example.calendartrainingevents;

import java.io.Serializable;

public class Event implements Serializable {
    private int id;
    private String title;
    private String dateTime;
    private String location;
    private String organizer;
    private String description;
    private int duration;
    private int color;

    public Event() {}

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}