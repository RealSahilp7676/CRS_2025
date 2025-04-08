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
import com.example.crs2025.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<Student> studentList;
    private List<Student> selectedStudents = new ArrayList<>();

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    public List<Student> getSelectedStudents() {
        return selectedStudents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.tvName.setText("Name: " + student.getName());
        holder.tvEmail.setText("Email: " + student.getEmail());
        holder.tvBranch.setText("Branch: " + student.getBranch());

        // 🔹 Detach previous listener to avoid incorrect checkbox movement
        holder.checkBox.setOnCheckedChangeListener(null);

        // 🔹 Set checkbox state correctly
        holder.checkBox.setChecked(selectedStudents.contains(student));

        // 🔹 Reattach listener to handle user interactions
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedStudents.add(student);
            } else {
                selectedStudents.remove(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // 🔹 Clear selections when delete operation is performed
    public void clearSelection() {
        selectedStudents.clear();
        notifyDataSetChanged(); // Refresh UI and reset checkboxes
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvBranch;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_student_name);
            tvEmail = itemView.findViewById(R.id.tv_student_email);
            tvBranch = itemView.findViewById(R.id.tv_student_branch);
            checkBox = itemView.findViewById(R.id.checkbox_select);
        }
    }
}
