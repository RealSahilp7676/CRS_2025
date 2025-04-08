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
import com.example.crs2025.models.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private Context context;
    private List<Application> applicationList;
    private List<Application> selectedApplications = new ArrayList<>();

    public ApplicationAdapter(Context context, List<Application> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    public List<Application> getSelectedApplications() {
        return selectedApplications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Application application = applicationList.get(position);
        holder.tvJobTitle.setText("Job: " + application.getJobTitle());
        holder.tvStudentName.setText("Student: " + application.getFullName());
        holder.tvCompanyName.setText("Company: " + application.getCompanyName());
        holder.tvStatus.setText("Status: " + application.getStatus());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedApplications.add(application);
            } else {
                selectedApplications.remove(application);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvStudentName, tvCompanyName, tvStatus;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tv_application_job_title);
            tvStudentName = itemView.findViewById(R.id.tv_application_student);
            tvCompanyName = itemView.findViewById(R.id.tv_application_company);
            tvStatus = itemView.findViewById(R.id.tv_application_status);
            checkBox = itemView.findViewById(R.id.checkbox_select);
        }
    }

    public void clearSelection() {
        selectedApplications.clear(); // Clear the selected applications list
        notifyDataSetChanged(); // Notify RecyclerView to refresh UI and reset checkboxes
    }

}
