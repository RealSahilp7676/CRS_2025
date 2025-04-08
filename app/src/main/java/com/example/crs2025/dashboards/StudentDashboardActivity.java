package com.example.crs2025.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.activities.LoginActivity;
import com.example.crs2025.student.SearchJobActivity;  // Import the SearchJobActivity
import com.example.crs2025.student.ApplyJobActivity;   // Import the ApplyJobActivity
import com.example.crs2025.student.TrackApplicationsActivity; // Import the TrackApplicationsActivity
import com.example.crs2025.student.ProvideStudentFeedbackActivity;  // Import the ProvideFeedbackActivity
import com.example.crs2025.models.User;
import com.example.crs2025.student.TrackInterviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnSearchJob, btnApplyJob, btnTrackStatus, btnTrackInterview, btnStudentFeedback, btnLogout;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        auth = FirebaseAuth.getInstance();
        studentId = Objects.requireNonNull(auth.getCurrentUser()).getUid(); // Get logged-in student ID
        userRef = FirebaseDatabase.getInstance().getReference("users").child("students").child(studentId);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnSearchJob = findViewById(R.id.btnSearchJob);
        btnApplyJob = findViewById(R.id.btnApplyJob);
        btnTrackStatus = findViewById(R.id.btnTrackStatus);
        btnTrackInterview = findViewById(R.id.btnTrackInterview);
        btnStudentFeedback = findViewById(R.id.btnStudentFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        loadStudentName();

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(StudentDashboardActivity.this, LoginActivity.class));
            finish();
        });

        // Setting up button listeners for activities
        btnSearchJob.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, SearchJobActivity.class);
            startActivity(intent);
        });

        btnApplyJob.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ApplyJobActivity.class);
            startActivity(intent);
        });

        btnTrackStatus.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, TrackApplicationsActivity.class);
            startActivity(intent);
        });

        btnTrackInterview.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, TrackInterviewActivity.class);
            startActivity(intent);
        });

        btnStudentFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ProvideStudentFeedbackActivity.class);
            startActivity(intent);
        });
    }

    private void loadStudentName() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User student = snapshot.getValue(User.class);
                    if (student != null) {
                        tvWelcome.setText("Welcome, " + student.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
