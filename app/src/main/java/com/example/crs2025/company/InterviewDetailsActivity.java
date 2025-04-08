package com.example.crs2025.company;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Interview;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InterviewDetailsActivity extends AppCompatActivity {

    private TextView tvJobTitle, tvStudentName, tvDate, tvTime, tvVenue, tvInterviewType, tvCompany;
    private Button btnGoBack;
    private DatabaseReference interviewsRef;
    private String interviewId, companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_details);

        tvJobTitle = findViewById(R.id.tv_job_title);
        tvStudentName = findViewById(R.id.tv_student_name);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvVenue = findViewById(R.id.tv_venue);
        tvInterviewType = findViewById(R.id.tv_interview_type);
        tvCompany = findViewById(R.id.tv_company);
        btnGoBack = findViewById(R.id.btn_go_back);

        interviewId = getIntent().getStringExtra("interviewId");
        companyId = getIntent().getStringExtra("companyId");

        interviewsRef = FirebaseDatabase.getInstance().getReference("interviews").child(companyId).child(interviewId);

        loadInterviewDetails();

        btnGoBack.setOnClickListener(v -> finish());
    }

    private void loadInterviewDetails() {
        interviewsRef.get().addOnSuccessListener(dataSnapshot -> {
            Interview interview = dataSnapshot.getValue(Interview.class);
            if (interview != null) {
                tvJobTitle.setText(interview.getJobTitle());
                tvStudentName.setText(interview.getStudentName());
                tvDate.setText(interview.getDate());
                tvTime.setText(interview.getTime());
                tvVenue.setText(interview.getVenue());
                tvInterviewType.setText(interview.getInterviewType());
                tvCompany.setText(interview.getCompanyName());
            }
        }).addOnFailureListener(e -> tvJobTitle.setText("Error loading details"));
    }
}
