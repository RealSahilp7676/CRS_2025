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
import com.example.crs2025.models.Interview;

import java.util.ArrayList;
import java.util.List;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.ViewHolder> {

    private Context context;
    private List<Interview> interviewList;
    private List<Interview> selectedInterviews = new ArrayList<>();

    public InterviewAdapter(Context context, List<Interview> interviewList) {
        this.context = context;
        this.interviewList = interviewList;
    }

    public List<Interview> getSelectedInterviews() {
        return selectedInterviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_interview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Interview interview = interviewList.get(position);
        holder.tvJobTitle.setText("Job: " + interview.getJobTitle());
        holder.tvCompanyName.setText("Company: " + interview.getCompanyName());
        holder.tvInterviewDate.setText("Date: " + interview.getDate());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedInterviews.add(interview);
            } else {
                selectedInterviews.remove(interview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCompanyName, tvInterviewDate;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tv_interview_job_title);
            tvCompanyName = itemView.findViewById(R.id.tv_interview_company_name);
            tvInterviewDate = itemView.findViewById(R.id.tv_interview_date);
            checkBox = itemView.findViewById(R.id.checkbox_select);
        }
    }
    public void clearSelection() {
        selectedInterviews.clear();
        notifyDataSetChanged();
    }
}
