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
import com.example.crs2025.adapters.StudentAdapter;
import com.example.crs2025.models.Student;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageStudentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private Button btnDeleteSelected, btnGoBack;
    private List<Student> studentList;
    private StudentAdapter adapter;
    private DatabaseReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        recyclerViewStudents = findViewById(R.id.recycler_students);
        btnDeleteSelected = findViewById(R.id.btn_delete_selected);
        btnGoBack = findViewById(R.id.btn_go_back);

        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList);
        recyclerViewStudents.setAdapter(adapter);

        studentsRef = FirebaseDatabase.getInstance().getReference("users").child("students");

        fetchStudents();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedStudents());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchStudents() {
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot studentSnap : snapshot.getChildren()) {
                    Student student = studentSnap.getValue(Student.class);
                    if (student != null) {
                        studentList.add(student);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageStudentsActivity.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedStudents() {
        List<Student> selectedStudents = adapter.getSelectedStudents();

        if (selectedStudents.isEmpty()) {
            Toast.makeText(this, "No students selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected students?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Student student : selectedStudents) {
                        // Delete from Firebase
                        Task<Void> deleteTask = studentsRef.child(student.getId()).removeValue();
                        deleteTasks.add(deleteTask);
                    }

                    // Wait for all deletions to complete before refreshing list
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                // Clear selections & Refresh UI
                                adapter.clearSelection();
                                fetchStudents();
                                Toast.makeText(this, "Selected students deleted successfully", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}
