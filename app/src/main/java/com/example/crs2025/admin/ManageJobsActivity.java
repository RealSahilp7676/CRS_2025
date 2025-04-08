package com.example.crs2025.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crs2025.R;
import com.example.crs2025.adapters.JobAdapter;
import com.example.crs2025.models.Job;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageJobsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewJobs;
    private Button btnDeleteSelected, btnGoBack;
    private List<Job> jobList;
    private JobAdapter adapter;
    private DatabaseReference jobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_jobs);

        recyclerViewJobs = findViewById(R.id.recycler_jobs);
        btnDeleteSelected = findViewById(R.id.btn_delete_selected);
        btnGoBack = findViewById(R.id.btn_go_back);

        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        adapter = new JobAdapter(this, jobList);
        recyclerViewJobs.setAdapter(adapter);

        jobsRef = FirebaseDatabase.getInstance().getReference("globalJobs");

        fetchJobs();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedJobs());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchJobs() {
        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    Job job = jobSnap.getValue(Job.class);
                    if (job != null) {
                        jobList.add(job);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageJobsActivity.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedJobs() {
        List<Job> selectedJobs = adapter.getSelectedJobs();

        if (selectedJobs.isEmpty()) {
            Toast.makeText(this, "No jobs selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected jobs?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Job job : selectedJobs) {
                        // Delete from jobs/{jobId}
                        Task<Void> deleteFromJobs = FirebaseDatabase.getInstance()
                                .getReference("jobs")
                                .child(job.getJobId())
                                .removeValue();

                        // Delete from globalJobs/{jobId}
                        Task<Void> deleteFromGlobalJobs = FirebaseDatabase.getInstance()
                                .getReference("globalJobs")
                                .child(job.getJobId())
                                .removeValue();

                        // Add tasks to the list
                        deleteTasks.add(deleteFromJobs);
                        deleteTasks.add(deleteFromGlobalJobs);
                    }

                    // Wait for all deletions to complete
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(this, "Selected jobs deleted successfully", Toast.LENGTH_SHORT).show();

                                // Clear selection in the adapter
                                adapter.clearSelection();

                                // Refresh the job list
                                fetchJobs();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
