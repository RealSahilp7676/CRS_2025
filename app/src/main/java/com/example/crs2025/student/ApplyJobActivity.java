//package com.example.crs2025.student;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.crs2025.R;
//import com.example.crs2025.models.Application;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ApplyJobActivity extends AppCompatActivity {
//
//    private Spinner spJobTitle, spCompany;
//    private TextView tvSkills;
//    private EditText etFullName, etEmail, etAddress, etBranch, etCgpa, etReason, etResume;
//    private Button btnApply, btnGoBack;
//    private DatabaseReference jobsRef, applicationsRef, globalAppsRef;
//    private FirebaseAuth mAuth;
//    private String selectedJobTitle, selectedCompanyId, selectedCompanyName, selectedSkills, selectedJobCgpa;
//    private Map<String, String> companyMap = new HashMap<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_apply_job);
//
//        // Initialize views
//        spJobTitle = findViewById(R.id.sp_job_title);
//        spCompany = findViewById(R.id.sp_company);
//        tvSkills = findViewById(R.id.tv_skills);
//        etFullName = findViewById(R.id.et_full_name);
//        etEmail = findViewById(R.id.et_email);
//        etAddress = findViewById(R.id.et_address);
//        etBranch = findViewById(R.id.et_branch);
//        etCgpa = findViewById(R.id.et_cgpa);
//        etReason = findViewById(R.id.et_reason);
//        etResume = findViewById(R.id.et_resume);
//        btnApply = findViewById(R.id.btn_apply);
//        btnGoBack = findViewById(R.id.btn_go_back);
//
//        // Initialize Firebase
//        mAuth = FirebaseAuth.getInstance();
//        jobsRef = FirebaseDatabase.getInstance().getReference("globalJobs");
//        applicationsRef = FirebaseDatabase.getInstance().getReference("applications");
//        globalAppsRef = FirebaseDatabase.getInstance().getReference("globalApplications");
//
//        // Load job titles dynamically
//        loadJobTitles();
//
//        // Handle job selection to load companies dynamically
//        spJobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedJobTitle = parent.getItemAtPosition(position).toString();
//                loadCompaniesForJob(selectedJobTitle);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        // Handle company selection to display job details
//        spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedCompanyName = parent.getItemAtPosition(position).toString();
//                selectedCompanyId = companyMap.get(selectedCompanyName);
//                fetchJobDetails(selectedCompanyId, selectedJobTitle);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        // Apply Job Button Click
//        btnApply.setOnClickListener(v -> applyForJob());
//        btnGoBack.setOnClickListener(v -> finish());
//    }
//
//    private void loadJobTitles() {
//        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> jobTitles = new ArrayList<>();
//                for (DataSnapshot jobSnap : snapshot.getChildren()) {
//                    String jobTitle = jobSnap.child("jobTitle").getValue(String.class);
//                    if (jobTitle != null && !jobTitles.contains(jobTitle)) {
//                        jobTitles.add(jobTitle);
//                    }
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(ApplyJobActivity.this, android.R.layout.simple_spinner_item, jobTitles);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spJobTitle.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void loadCompaniesForJob(String jobTitle) {
//        jobsRef.orderByChild("jobTitle").equalTo(jobTitle).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> companyNames = new ArrayList<>();
//                companyMap.clear();
//                for (DataSnapshot jobSnap : snapshot.getChildren()) {
//                    String companyId = jobSnap.child("companyId").getValue(String.class);
//                    String companyName = jobSnap.child("companyName").getValue(String.class);
//                    if (companyId != null && companyName != null) {
//                        companyNames.add(companyName);
//                        companyMap.put(companyName, companyId);
//                    }
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(ApplyJobActivity.this, android.R.layout.simple_spinner_item, companyNames);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spCompany.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void fetchJobDetails(String companyId, String jobTitle) {
//        jobsRef.orderByChild("companyId").equalTo(companyId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot jobSnap : snapshot.getChildren()) {
//                    if (jobSnap.child("jobTitle").getValue(String.class).equals(jobTitle)) {
//                        selectedSkills = jobSnap.child("skills").getValue(String.class);
//                        selectedJobCgpa = jobSnap.child("cgpa").getValue(String.class);
//                        tvSkills.setText(selectedSkills);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void applyForJob() {
//        String fullName = etFullName.getText().toString();
//        String email = etEmail.getText().toString();
//        String address = etAddress.getText().toString();
//        String branch = etBranch.getText().toString();
//        String cgpa = etCgpa.getText().toString();
//        String reason = etReason.getText().toString();
//        String resume = etResume.getText().toString();
//
//        if (Double.parseDouble(cgpa) < Double.parseDouble(selectedJobCgpa)) {
//            Toast.makeText(this, "You do not meet the CGPA requirement.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String applicationId = applicationsRef.push().getKey();
//        FirebaseUser user = mAuth.getCurrentUser();
//        String studentId = (user != null) ? user.getUid() : "Unknown";
//
//        Application application = new Application(applicationId, studentId, selectedJobTitle, selectedCompanyId,
//                selectedJobTitle, selectedCompanyName, selectedSkills, fullName, email, address, branch, cgpa,
//                reason, resume, "Pending");
//
//        applicationsRef.child(selectedCompanyId).child(applicationId).setValue(application);
//        globalAppsRef.child(applicationId).setValue(application);
//
//        Toast.makeText(this, "Application Submitted!", Toast.LENGTH_SHORT).show();
//        finish();
//    }
//}

package com.example.crs2025.student;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyJobActivity extends AppCompatActivity {

    private Spinner spJobTitle, spCompany;
    private TextView tvSkills;
    private EditText etFullName, etEmail, etAddress, etBranch, etCgpa, etReason, etResume;
    private Button btnApply, btnGoBack;
    private DatabaseReference jobsRef, applicationsRef, globalAppsRef;
    private FirebaseAuth mAuth;
    private String selectedJobTitle, selectedCompanyId, selectedCompanyName, selectedSkills, selectedJobCgpa;
    private Map<String, List<JobDetails>> jobMap = new HashMap<>();
    private Map<String, JobDetails> companyJobMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        // Initialize views
        spJobTitle = findViewById(R.id.sp_job_title);
        spCompany = findViewById(R.id.sp_company);
        tvSkills = findViewById(R.id.tv_skills);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etBranch = findViewById(R.id.et_branch);
        etCgpa = findViewById(R.id.et_cgpa);
        etReason = findViewById(R.id.et_reason);
        etResume = findViewById(R.id.et_resume);
        btnApply = findViewById(R.id.btn_apply);
        btnGoBack = findViewById(R.id.btn_go_back);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        jobsRef = FirebaseDatabase.getInstance().getReference("globalJobs");
        applicationsRef = FirebaseDatabase.getInstance().getReference("applications");
        globalAppsRef = FirebaseDatabase.getInstance().getReference("globalApplications");

        // Load job titles dynamically
        loadJobTitles();

        // Handle job selection to load companies dynamically
        spJobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedJobTitle = parent.getItemAtPosition(position).toString();
                loadCompaniesForJob(selectedJobTitle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Handle company selection to display job details
        spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCompanyName = parent.getItemAtPosition(position).toString();
                JobDetails jobDetails = companyJobMap.get(selectedCompanyName);
                if (jobDetails != null) {
                    selectedCompanyId = jobDetails.companyId;
                    selectedSkills = jobDetails.skills;
                    selectedJobCgpa = jobDetails.cgpa;
                    tvSkills.setText(selectedSkills);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Apply Job Button Click
        btnApply.setOnClickListener(v -> applyForJob());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void loadJobTitles() {
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> jobTitles = new ArrayList<>();
                jobMap.clear();

                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    String jobId = jobSnap.getKey();
                    String jobTitle = jobSnap.child("jobTitle").getValue(String.class);
                    String companyId = jobSnap.child("companyId").getValue(String.class);
                    String companyName = jobSnap.child("companyName").getValue(String.class);
                    String skills = jobSnap.child("skills").getValue(String.class);
                    String cgpa = jobSnap.child("cgpa").getValue(String.class);

                    if (jobTitle != null && companyId != null && companyName != null) {
                        JobDetails jobDetails = new JobDetails(jobId, companyId, companyName, skills, cgpa);
                        jobMap.putIfAbsent(jobTitle, new ArrayList<>());
                        jobMap.get(jobTitle).add(jobDetails);

                        if (!jobTitles.contains(jobTitle)) {
                            jobTitles.add(jobTitle);
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ApplyJobActivity.this, android.R.layout.simple_spinner_item, jobTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spJobTitle.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadCompaniesForJob(String jobTitle) {
        List<JobDetails> companyList = jobMap.get(jobTitle);
        if (companyList != null) {
            List<String> companyNames = new ArrayList<>();
            companyJobMap.clear();
            for (JobDetails jobDetails : companyList) {
                companyNames.add(jobDetails.companyName);
                companyJobMap.put(jobDetails.companyName, jobDetails);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(ApplyJobActivity.this, android.R.layout.simple_spinner_item, companyNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCompany.setAdapter(adapter);
        }
    }

    private void applyForJob() {
        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();
        String branch = etBranch.getText().toString();
        String cgpa = etCgpa.getText().toString();
        String reason = etReason.getText().toString();
        String resume = etResume.getText().toString();

        if (selectedJobCgpa != null && Double.parseDouble(cgpa) < Double.parseDouble(selectedJobCgpa)) {
            Toast.makeText(this, "You do not meet the CGPA requirement.", Toast.LENGTH_SHORT).show();
            return;
        }

        String applicationId = applicationsRef.push().getKey();
        FirebaseUser user = mAuth.getCurrentUser();
        String studentId = (user != null) ? user.getUid() : "Unknown";

        Application application = new Application(applicationId, studentId, selectedJobTitle, selectedCompanyId,
                selectedJobTitle, selectedCompanyName, selectedSkills, fullName, email, address, branch, cgpa,
                reason, resume, "Pending");

        applicationsRef.child(selectedCompanyId).child(applicationId).setValue(application);
        globalAppsRef.child(applicationId).setValue(application);

        Toast.makeText(this, "Application Submitted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Helper class for storing job details
    private static class JobDetails {
        String jobId, companyId, companyName, skills, cgpa;

        JobDetails(String jobId, String companyId, String companyName, String skills, String cgpa) {
            this.jobId = jobId;
            this.companyId = companyId;
            this.companyName = companyName;
            this.skills = skills;
            this.cgpa = cgpa;
        }
    }
}
