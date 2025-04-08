package com.example.crs2025.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crs2025.R;
import com.example.crs2025.adapters.FeedbackAdapter;
import com.example.crs2025.models.Feedback;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageFeedbacksActivity extends AppCompatActivity {

    private RecyclerView recyclerStudentFeedbacks, recyclerCompanyFeedbacks;
    private Button btnDeleteStudentFeedbacks, btnDeleteCompanyFeedbacks, btnGoBack;
    private List<Feedback> studentFeedbackList, companyFeedbackList;
    private FeedbackAdapter studentFeedbackAdapter, companyFeedbackAdapter;
    private DatabaseReference studentFeedbackRef, companyFeedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feedbacks);

        recyclerStudentFeedbacks = findViewById(R.id.recycler_student_feedbacks);
        recyclerCompanyFeedbacks = findViewById(R.id.recycler_company_feedbacks);
        btnDeleteStudentFeedbacks = findViewById(R.id.btn_delete_student_feedbacks);
        btnDeleteCompanyFeedbacks = findViewById(R.id.btn_delete_company_feedbacks);
        btnGoBack = findViewById(R.id.btn_go_back);

        recyclerStudentFeedbacks.setLayoutManager(new LinearLayoutManager(this));
        recyclerCompanyFeedbacks.setLayoutManager(new LinearLayoutManager(this));

        studentFeedbackList = new ArrayList<>();
        companyFeedbackList = new ArrayList<>();

        studentFeedbackAdapter = new FeedbackAdapter(this, studentFeedbackList);
        companyFeedbackAdapter = new FeedbackAdapter(this, companyFeedbackList);

        recyclerStudentFeedbacks.setAdapter(studentFeedbackAdapter);
        recyclerCompanyFeedbacks.setAdapter(companyFeedbackAdapter);

        studentFeedbackRef = FirebaseDatabase.getInstance().getReference("feedback/students");
        companyFeedbackRef = FirebaseDatabase.getInstance().getReference("feedback/companies");

        fetchStudentFeedbacks();
        fetchCompanyFeedbacks();

        btnDeleteStudentFeedbacks.setOnClickListener(v -> deleteSelectedStudentFeedbacks());
        btnDeleteCompanyFeedbacks.setOnClickListener(v -> deleteSelectedCompanyFeedbacks());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchStudentFeedbacks() {
        studentFeedbackList.clear();

        studentFeedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentFeedbackList.clear();
                for (DataSnapshot feedbackSnap : snapshot.getChildren()) {
                    Feedback feedback = feedbackSnap.getValue(Feedback.class);
                    if (feedback != null) {
                        feedback.setFeedbackId(feedbackSnap.getKey());
                        studentFeedbackList.add(feedback);
                    }
                }
                studentFeedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageFeedbacksActivity.this, "Failed to fetch student feedbacks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCompanyFeedbacks() {
        companyFeedbackList.clear();

        companyFeedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                companyFeedbackList.clear();
                for (DataSnapshot companySnap : snapshot.getChildren()) {
                    String companyId = companySnap.getKey();

                    for (DataSnapshot feedbackSnap : companySnap.getChildren()) {
                        Feedback feedback = feedbackSnap.getValue(Feedback.class);
                        if (feedback != null) {
                            feedback.setFeedbackId(feedbackSnap.getKey());
                            feedback.setUserId(companyId);
                            companyFeedbackList.add(feedback);
                        }
                    }
                }
                companyFeedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageFeedbacksActivity.this, "Failed to fetch company feedbacks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedStudentFeedbacks() {
        List<Feedback> selectedFeedbacks = studentFeedbackAdapter.getSelectedFeedbacks();

        if (selectedFeedbacks.isEmpty()) {
            Toast.makeText(this, "No student feedback selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected student feedbacks?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Feedback feedback : selectedFeedbacks) {
                        // Delete from Firebase
                        Task<Void> deleteTask = studentFeedbackRef.child(feedback.getFeedbackId()).removeValue();
                        deleteTasks.add(deleteTask);
                    }

                    // Wait for all deletions to complete before refreshing list
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                // Clear selections & Refresh UI
                                studentFeedbackAdapter.clearSelection();
                                fetchStudentFeedbacks();
                                Toast.makeText(this, "Selected student feedback deleted", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteSelectedCompanyFeedbacks() {
        List<Feedback> selectedFeedbacks = companyFeedbackAdapter.getSelectedFeedbacks();

        if (selectedFeedbacks.isEmpty()) {
            Toast.makeText(this, "No company feedback selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected company feedbacks?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Feedback feedback : selectedFeedbacks) {
                        // Delete from Firebase
                        Task<Void> deleteTask = companyFeedbackRef.child(feedback.getUserId())
                                .child(feedback.getFeedbackId())
                                .removeValue();
                        deleteTasks.add(deleteTask);
                    }

                    // Wait for all deletions to complete before refreshing list
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                // Clear selections & Refresh UI
                                companyFeedbackAdapter.clearSelection();
                                fetchCompanyFeedbacks();
                                Toast.makeText(this, "Selected company feedback deleted", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
