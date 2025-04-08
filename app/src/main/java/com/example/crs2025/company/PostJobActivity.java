package com.example.crs2025.company;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostJobActivity extends AppCompatActivity {

    private EditText etJobTitle, etSkills, etCgpa, etJobType;
    private CheckBox cbInternship;
    private Button btnPostJob, btnGoBack;

    private DatabaseReference jobDatabase;
    private DatabaseReference globalJobRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        // Initialize views
        etJobTitle = findViewById(R.id.et_job_title);
        etSkills = findViewById(R.id.et_skills);
        etCgpa = findViewById(R.id.et_cgpa);
        etJobType = findViewById(R.id.et_job_type);
        cbInternship = findViewById(R.id.cb_internship);
        btnPostJob = findViewById(R.id.btn_post_job);
        btnGoBack = findViewById(R.id.btn_go_back);

        // Initialize Firebase references
        mAuth = FirebaseAuth.getInstance();
        jobDatabase = FirebaseDatabase.getInstance().getReference("jobs");
        globalJobRef = FirebaseDatabase.getInstance().getReference("globalJobs");

        // Set up the "Post Job" button click listener
        btnPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postJob();
            }
        });
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void postJob() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String companyId = firebaseUser.getUid(); // Get unique company UID

        // Reference to fetch company name
        DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("users")
                .child("companies")
                .child(companyId)
                .child("name");

        companyRef.get().addOnSuccessListener(dataSnapshot -> {
            String companyName = dataSnapshot.getValue(String.class);

            if (companyName == null || companyName.isEmpty()) {
                companyName = "Unknown"; // Fallback if company name is missing
            }

            // Get job details from EditText fields
            String jobTitle = etJobTitle.getText().toString().trim();
            String skills = etSkills.getText().toString().trim();
            String cgpa = etCgpa.getText().toString().trim();
            String jobType = etJobType.getText().toString().trim();
            boolean isInternship = cbInternship.isChecked();

            // Validate input fields
            if (TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(skills) ||
                    TextUtils.isEmpty(cgpa) || TextUtils.isEmpty(jobType)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate unique job ID
            DatabaseReference companyJobRef = jobDatabase.child(companyId);
            String jobId = companyJobRef.push().getKey();

            if (jobId == null) {
                Toast.makeText(this, "Error generating job ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a Job object with all details, including company name
            Job job = new Job(jobId, companyId, companyName, jobTitle, skills, cgpa, jobType, isInternship);

            // Store job under company-specific jobs collection
            companyJobRef.child(jobId).setValue(job);

            // Store job in global jobs collection
            globalJobRef.child(jobId).setValue(job)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Job Posted Successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after posting
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to post job", Toast.LENGTH_SHORT).show()
                    );

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch company name", Toast.LENGTH_SHORT).show();
        });
    }

}
