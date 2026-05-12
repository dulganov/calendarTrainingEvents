package com.example.calendartrainingevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<Event> events) {
        this.events = new ArrayList<>(events);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final View typeIndicator;
        private final TextView tvDiscipline;
        private final TextView tvTitle;
        private final TextView tvTime;
        private final TextView tvTeacher;
        private final TextView tvAudience;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeIndicator = itemView.findViewById(R.id.typeIndicator);
            tvDiscipline = itemView.findViewById(R.id.tvDiscipline);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTeacher = itemView.findViewById(R.id.tvTeacher);
            tvAudience = itemView.findViewById(R.id.tvAudience);
        }

        void bind(Event event) {
            typeIndicator.setBackgroundColor(event.getColor());
            tvDiscipline.setText("Событие колледжа");
            tvTitle.setText(event.getTitle());
            tvTime.setText(extractTime(event.getDateTime()));
            tvTeacher.setText(event.getOrganizer());
            tvAudience.setText(event.getLocation());
        }

        private String extractTime(String dateTime) {
            if (dateTime == null || dateTime.trim().isEmpty()) {
                return "";
            }

            String[] parts = dateTime.trim().split("\\s+");
            return parts.length > 1 ? parts[1] : dateTime;
        }
    }
}
