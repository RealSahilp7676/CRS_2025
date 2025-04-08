package com.example.crs2025.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crs2025.R;
import com.example.crs2025.models.Feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private Context context;
    private List<Feedback> feedbackList;
    private List<Feedback> selectedFeedbacks = new ArrayList<>();

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    public List<Feedback> getSelectedFeedbacks() {
        return selectedFeedbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.tvName.setText("Name: " + feedback.getName());
        holder.tvEmail.setText("Email: " + feedback.getEmail());
        holder.tvRating.setText("Satisfaction: " + feedback.getSatisfaction());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedFeedbacks.add(feedback);
            } else {
                selectedFeedbacks.remove(feedback);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRating;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_feedback_name);
            tvEmail = itemView.findViewById(R.id.tv_feedback_email);
            tvRating = itemView.findViewById(R.id.tv_feedback_rating);
            checkBox = itemView.findViewById(R.id.checkbox_select);
        }
    }

    public void clearSelection() {
        selectedFeedbacks.clear();
        notifyDataSetChanged(); // Ensures checkboxes are reset properly
    }

}
