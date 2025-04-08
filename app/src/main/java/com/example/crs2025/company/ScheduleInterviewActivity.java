package com.example.crs2025.company;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Interview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleInterviewActivity extends AppCompatActivity {

    private Spinner spJobTitle, spStudentName, spInterviewType;
    private EditText etVenue, etDate, etTime;
    private Button btnSchedule, btnPreviousInterviews, btnGoBack;
    private DatabaseReference applicationsRef, interviewsRef, globalInterviewsRef, jobsRef, usersRef;
    private FirebaseAuth mAuth;
    private String companyId, companyName;
    private String selectedJobTitle, selectedStudentId, selectedStudentName, selectedInterviewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_interview);

        spJobTitle = findViewById(R.id.sp_job_title);
        spStudentName = findViewById(R.id.sp_student_name);
        spInterviewType = findViewById(R.id.sp_interview_type);
        etVenue = findViewById(R.id.et_venue);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        btnSchedule = findViewById(R.id.btn_schedule);
        btnPreviousInterviews = findViewById(R.id.btn_previous_interviews);
        btnGoBack = findViewById(R.id.btn_go_back);
        btnGoBack.setOnClickListener(v -> finish());

        mAuth = FirebaseAuth.getInstance();
        companyId = mAuth.getCurrentUser().getUid();
        applicationsRef = FirebaseDatabase.getInstance().getReference("applications").child(companyId);
        interviewsRef = FirebaseDatabase.getInstance().getReference("interviews").child(companyId);
        globalInterviewsRef = FirebaseDatabase.getInstance().getReference("globalInterviews");
        jobsRef = FirebaseDatabase.getInstance().getReference("jobs").child(companyId);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        loadCompanyDetails();
        loadJobTitles();
        setupInterviewTypeSpinner();

        spJobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedJobTitle = parent.getItemAtPosition(position).toString();
                loadApprovedStudents(selectedJobTitle);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spStudentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStudentName = parent.getItemAtPosition(position).toString();
                selectedStudentId = (String) spStudentName.getTag(); // Storing student ID
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spInterviewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedInterviewType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnSchedule.setOnClickListener(v -> scheduleInterview());

        btnPreviousInterviews.setOnClickListener(v -> showPreviousInterviews());
    }

    private void loadCompanyDetails() {
        usersRef.child("companies").child(companyId).child("name").get().addOnSuccessListener(dataSnapshot -> {
            companyName = dataSnapshot.getValue(String.class);
            if (companyName == null) companyName = "Unknown";
        });
    }

    private void loadJobTitles() {
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> jobTitles = new ArrayList<>();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    String jobTitle = jobSnap.child("jobTitle").getValue(String.class);
                    if (jobTitle != null) jobTitles.add(jobTitle);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ScheduleInterviewActivity.this, android.R.layout.simple_spinner_item, jobTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spJobTitle.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadApprovedStudents(String jobTitle) {
        applicationsRef.orderByChild("jobTitle").equalTo(jobTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> studentNames = new ArrayList<>();
                for (DataSnapshot appSnap : snapshot.getChildren()) {
                    String studentId = appSnap.child("studentId").getValue(String.class);
                    String studentName = appSnap.child("fullName").getValue(String.class);
                    String status = appSnap.child("status").getValue(String.class);
                    if (studentId != null && studentName != null && "Approved".equals(status)) {
                        studentNames.add(studentName);
                        spStudentName.setTag(studentId); // Store student ID for later use
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ScheduleInterviewActivity.this, android.R.layout.simple_spinner_item, studentNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spStudentName.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> etTime.setText(hourOfDay + ":" + minute), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void setupInterviewTypeSpinner() {
        // Static array for Interview Type
        String[] interviewTypes = {"Online", "Offline"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, interviewTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInterviewType.setAdapter(adapter);

        // Default value set to Online
        spInterviewType.setSelection(0);
        selectedInterviewType = "Online";
    }
    private void scheduleInterview() {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String venue = etVenue.getText().toString();

        if (date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String interviewId = interviewsRef.push().getKey();
        Interview interview = new Interview(interviewId, companyId, companyName, selectedJobTitle, selectedStudentId, selectedStudentName, date, time, venue, selectedInterviewType);

        interviewsRef.child(interviewId).setValue(interview);
        globalInterviewsRef.child(interviewId).setValue(interview)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Interview Scheduled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to schedule interview", Toast.LENGTH_SHORT).show());
    }

    private void showPreviousInterviews() {
        startActivity(new Intent(ScheduleInterviewActivity.this, PreviousInterviewsActivity.class));
    }
}
