package com.example.crs2025.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApplicationDetailsForCompanyActivity extends AppCompatActivity {

    private TextView tvJobTitle, tvCompany, tvSkills, tvFullName, tvEmail, tvAddress, tvBranch, tvCgpa, tvReason, tvResume, tvStatus;
    private Button btnApprove, btnReject, btnGoBack;
    private DatabaseReference applicationsRef, globalAppsRef;
    private String applicationId, companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details_for_company);

        tvJobTitle = findViewById(R.id.tv_job_title);
        tvFullName = findViewById(R.id.tv_full_name);
        tvEmail = findViewById(R.id.tv_email);
        tvAddress = findViewById(R.id.tv_address);
        tvBranch = findViewById(R.id.tv_branch);
        tvCgpa = findViewById(R.id.tv_cgpa);
        tvReason = findViewById(R.id.tv_reason);
        tvResume = findViewById(R.id.tv_resume);
        tvStatus = findViewById(R.id.tv_status);
        btnApprove = findViewById(R.id.btn_approve);
        btnReject = findViewById(R.id.btn_reject);
        btnGoBack = findViewById(R.id.btn_go_back);

        applicationsRef = FirebaseDatabase.getInstance().getReference("applications");
        globalAppsRef = FirebaseDatabase.getInstance().getReference("globalApplications");

        applicationId = getIntent().getStringExtra("applicationId");
        companyId = getIntent().getStringExtra("companyId");

        loadApplicationDetails();

        btnApprove.setOnClickListener(v -> updateApplicationStatus("Approved"));
        btnReject.setOnClickListener(v -> updateApplicationStatus("Rejected"));
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void loadApplicationDetails() {
        applicationsRef.child(companyId).child(applicationId).get().addOnSuccessListener(dataSnapshot -> {
            Application application = dataSnapshot.getValue(Application.class);
            if (application != null) {
                tvJobTitle.setText(application.getJobTitle());
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

    private void updateApplicationStatus(String status) {
        applicationsRef.child(companyId).child(applicationId).child("status").setValue(status);
        globalAppsRef.child(applicationId).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    tvStatus.setText(status);
                    Toast.makeText(this, "Application " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show());
    }
}
