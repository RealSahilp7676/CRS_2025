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
import com.example.crs2025.adapters.ApplicationAdapter;
import com.example.crs2025.models.Application;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageApplicationsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewApplications;
    private Button btnDeleteSelected, btnGoBack;
    private List<Application> applicationList;
    private ApplicationAdapter applicationAdapter;
    private DatabaseReference applicationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_applications);

        recyclerViewApplications = findViewById(R.id.recycler_applications);
        btnDeleteSelected = findViewById(R.id.btn_delete_selected);
        btnGoBack = findViewById(R.id.btn_go_back);

        recyclerViewApplications.setLayoutManager(new LinearLayoutManager(this));

        applicationList = new ArrayList<>();
        applicationAdapter = new ApplicationAdapter(this, applicationList);
        recyclerViewApplications.setAdapter(applicationAdapter);

        applicationsRef = FirebaseDatabase.getInstance().getReference("applications");

        fetchApplications();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedApplications());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchApplications() {
        applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList.clear();

                for (DataSnapshot companySnap : snapshot.getChildren()) {
                    for (DataSnapshot applicationSnap : companySnap.getChildren()) {
                        Application application = applicationSnap.getValue(Application.class);

                        if (application != null) {
                            applicationList.add(application);
                        }
                    }
                }

                if (applicationList.isEmpty()) {
                    Toast.makeText(ManageApplicationsActivity.this, "No applications found", Toast.LENGTH_SHORT).show();
                }
                applicationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageApplicationsActivity.this, "Failed to fetch applications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedApplications() {
        List<Application> selectedApplications = applicationAdapter.getSelectedApplications();

        if (selectedApplications.isEmpty()) {
            Toast.makeText(this, "No applications selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected applications?")
                .setPositiveButton("Delete", (dialog, which) -> {

                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Application application : selectedApplications) {
                        // Delete from applications/{companyId}/{applicationId}
                        Task<Void> deleteFromApplications = applicationsRef
                                .child(application.getCompanyId())
                                .child(application.getApplicationId())
                                .removeValue();

                        // Delete from globalApplications/{applicationId}
                        Task<Void> deleteFromGlobalApplications = FirebaseDatabase.getInstance()
                                .getReference("globalApplications")
                                .child(application.getApplicationId())
                                .removeValue();

                        // Add both tasks to list
                        deleteTasks.add(deleteFromApplications);
                        deleteTasks.add(deleteFromGlobalApplications);
                    }

                    // Wait for all deletions to complete
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(this, "Selected applications deleted successfully", Toast.LENGTH_SHORT).show();

                                applicationAdapter.clearSelection();
                                // Refresh the list
                                fetchApplications();
                            });

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
