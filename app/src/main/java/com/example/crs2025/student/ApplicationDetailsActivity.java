package com.example.crs2025.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApplicationDetailsActivity extends AppCompatActivity {

    private TextView tvJobTitle, tvCompany, tvSkills, tvFullName, tvEmail, tvAddress, tvBranch, tvCgpa, tvReason, tvResume, tvStatus;
    private Button btnGoBack;
    private DatabaseReference applicationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);

        tvJobTitle = findViewById(R.id.tv_job_title);
        tvCompany = findViewById(R.id.tv_company);
        tvSkills = findViewById(R.id.tv_skills);
        tvFullName = findViewById(R.id.tv_full_name);
        tvEmail = findViewById(R.id.tv_email);
        tvAddress = findViewById(R.id.tv_address);
        tvBranch = findViewById(R.id.tv_branch);
        tvCgpa = findViewById(R.id.tv_cgpa);
        tvReason = findViewById(R.id.tv_reason);
        tvResume = findViewById(R.id.tv_resume);
        tvStatus = findViewById(R.id.tv_status);
        btnGoBack = findViewById(R.id.btn_go_back);

        applicationsRef = FirebaseDatabase.getInstance().getReference("globalApplications");

        String applicationId = getIntent().getStringExtra("applicationId");
        loadApplicationDetails(applicationId);

        btnGoBack.setOnClickListener(v -> finish());
    }

    private void loadApplicationDetails(String applicationId) {
        applicationsRef.child(applicationId).get().addOnSuccessListener(dataSnapshot -> {
            Application application = dataSnapshot.getValue(Application.class);
            if (application != null) {
                tvJobTitle.setText(application.getJobTitle());
                tvCompany.setText(application.getCompanyName());
                tvSkills.setText(application.getSkills());
                tvFullName.setText(application.getFullName());
                tvEmail.setText(application.getEmail());
                tvAddress.setText(application.getAddress());
                tvBranch.setText(application.getBranch());
                tvCgpa.setText(application.getCgpa());
                tvReason.setText(application.getReasonToApply());
                tvResume.setText(application.getResumeLink());
                tvStatus.setText(application.getStatus());
            }
        }).addOnFailureListener(e -> tvJobTitle.setText("Error loading details"));
    }
}
