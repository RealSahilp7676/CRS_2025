package com.example.crs2025.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.activities.LoginActivity;
import com.example.crs2025.company.PostJobActivity;
import com.example.crs2025.company.ProvideCompanyFeedbackActivity;
import com.example.crs2025.company.ReviewApplicationsActivity;
import com.example.crs2025.company.ScheduleInterviewActivity;
import com.example.crs2025.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CompanyDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnPostJob, btnReviewApplications, btnScheduleInterview, btnCompanyFeedback, btnLogout;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            // Redirect to login if no user is found
            startActivity(new Intent(CompanyDashboardActivity.this, LoginActivity.class));
            finish();
            return;
        }

        companyId = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child("companies").child(companyId);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnPostJob = findViewById(R.id.btnPostJob);
        btnReviewApplications = findViewById(R.id.btnReviewApplications);
        btnScheduleInterview = findViewById(R.id.btnScheduleInterview);
        btnCompanyFeedback = findViewById(R.id.btnCompanyFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        loadCompanyName();

        // Post Job button functionality
        btnPostJob.setOnClickListener(v -> {
            // Navigate to the Post Job Activity
            Intent intent = new Intent(CompanyDashboardActivity.this, PostJobActivity.class);
            startActivity(intent);
        });

        // Review Applications button functionality
        btnReviewApplications.setOnClickListener(v -> {
            // Navigate to the Review Applications Activity
            Intent intent = new Intent(CompanyDashboardActivity.this, ReviewApplicationsActivity.class);
            startActivity(intent);
        });

        // Schedule Interview button functionality
        btnScheduleInterview.setOnClickListener(v -> {
            // Navigate to the Schedule Interview Activity
            Intent intent = new Intent(CompanyDashboardActivity.this, ScheduleInterviewActivity.class);
            startActivity(intent);
        });

        // Feedback button functionality
        btnCompanyFeedback.setOnClickListener(v -> {
            // Navigate to the Feedback Activity
            Intent intent = new Intent(CompanyDashboardActivity.this, ProvideCompanyFeedbackActivity.class);
            startActivity(intent);
            });
        // Logout button functionality
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(CompanyDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }


    private void loadCompanyName() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User company = snapshot.getValue(User.class);
                    if (company != null) {
                        tvWelcome.setText("Welcome, " + company.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
