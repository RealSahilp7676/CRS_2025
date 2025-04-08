package com.example.crs2025.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.admin.ManageStudentsActivity;
import com.example.crs2025.admin.ManageCompaniesActivity;
import com.example.crs2025.admin.ManageApplicationsActivity;
import com.example.crs2025.admin.ManageJobsActivity;
import com.example.crs2025.admin.ManageInterviewsActivity;
import com.example.crs2025.admin.ManageFeedbacksActivity;
import com.example.crs2025.activities.LoginActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageStudents, btnManageCompanies, btnManageApplications, btnManageJobs, btnManageInterviews, btnManageFeedbacks, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize buttons
        btnManageStudents = findViewById(R.id.btn_manage_students);
        btnManageCompanies = findViewById(R.id.btn_manage_companies);
        btnManageApplications = findViewById(R.id.btn_manage_applications);
        btnManageJobs = findViewById(R.id.btn_manage_jobs);
        btnManageInterviews = findViewById(R.id.btn_manage_interviews);
        btnManageFeedbacks = findViewById(R.id.btn_manage_feedbacks);
        btnLogout = findViewById(R.id.btn_logout);

        // Set click listeners
        btnManageStudents.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, ManageStudentsActivity.class)));
        btnManageCompanies.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, ManageCompaniesActivity.class)));
        btnManageApplications.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, ManageApplicationsActivity.class)));
        btnManageJobs.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, ManageJobsActivity.class)));
        btnManageInterviews.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, ManageInterviewsActivity.class)));
        btnManageFeedbacks.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, ManageFeedbacksActivity.class)));

        btnLogout.setOnClickListener(v -> {
            // Logout logic (Redirect to LoginActivity)
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
