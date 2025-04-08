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
import com.example.crs2025.adapters.InterviewAdapter;
import com.example.crs2025.models.Interview;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageInterviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInterviews;
    private Button btnDeleteSelected, btnGoBack;
    private List<Interview> interviewList;
    private InterviewAdapter adapter;
    private DatabaseReference interviewsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_interviews);

        recyclerViewInterviews = findViewById(R.id.recycler_interviews);
        btnDeleteSelected = findViewById(R.id.btn_delete_selected);
        btnGoBack = findViewById(R.id.btn_go_back);

        recyclerViewInterviews.setLayoutManager(new LinearLayoutManager(this));
        interviewList = new ArrayList<>();
        adapter = new InterviewAdapter(this, interviewList);
        recyclerViewInterviews.setAdapter(adapter);

        interviewsRef = FirebaseDatabase.getInstance().getReference("globalInterviews");

        fetchInterviews();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedInterviews());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchInterviews() {
        interviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                interviewList.clear();
                for (DataSnapshot interviewSnap : snapshot.getChildren()) {
                    Interview interview = interviewSnap.getValue(Interview.class);
                    if (interview != null) {
                        interviewList.add(interview);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageInterviewsActivity.this, "Failed to load interviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedInterviews() {
        List<Interview> selectedInterviews = adapter.getSelectedInterviews();

        if (selectedInterviews.isEmpty()) {
            Toast.makeText(this, "No interviews selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected interviews?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Interview interview : selectedInterviews) {
                        // Delete from interviews/{companyId}/{interviewId}
                        Task<Void> deleteFromInterviews = FirebaseDatabase.getInstance()
                                .getReference("interviews")
                                .child(interview.getCompanyId()) // Assuming interviews are stored per company
                                .child(interview.getInterviewId())
                                .removeValue();

                        // Delete from globalInterviews/{interviewId}
                        Task<Void> deleteFromGlobalInterviews = FirebaseDatabase.getInstance()
                                .getReference("globalInterviews")
                                .child(interview.getInterviewId())
                                .removeValue();

                        // Add both tasks to the list
                        deleteTasks.add(deleteFromInterviews);
                        deleteTasks.add(deleteFromGlobalInterviews);
                    }

                    // Wait for all deletions to complete
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                Toast.makeText(this, "Selected interviews deleted successfully", Toast.LENGTH_SHORT).show();

                                // Clear selection in the adapter
                                adapter.clearSelection();

                                // Refresh the interview list
                                fetchInterviews();
                            });

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
