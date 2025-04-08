package com.example.crs2025.student;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Feedback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ProvideStudentFeedbackActivity extends AppCompatActivity {

    private TextView tvStudentName, tvEmail;
    private RadioGroup rgSatisfaction, rgRecommendation;
    private EditText etImprovement, etComments;
    private Button btnSubmit, btnGoBack;
    private DatabaseReference feedbackRef, usersRef;
    private FirebaseAuth mAuth;
    private String userId, studentName, studentEmail, selectedSatisfaction, selectedRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_student_feedback);

        tvStudentName = findViewById(R.id.tv_student_name);
        tvEmail = findViewById(R.id.tv_email);
        rgSatisfaction = findViewById(R.id.rg_satisfaction);
        rgRecommendation = findViewById(R.id.rg_recommendation);
        etImprovement = findViewById(R.id.et_improvement);
        etComments = findViewById(R.id.et_comments);
        btnSubmit = findViewById(R.id.btn_submit);
        btnGoBack = findViewById(R.id.btn_go_back);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("users").child("students").child(userId);
        feedbackRef = FirebaseDatabase.getInstance().getReference("feedback/students");

        loadStudentDetails();

        rgSatisfaction.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            selectedSatisfaction = selectedButton.getText().toString();
        });

        rgRecommendation.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            selectedRecommendation = selectedButton.getText().toString();
        });

        btnSubmit.setOnClickListener(v -> submitFeedback());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void loadStudentDetails() {
        usersRef.get().addOnSuccessListener(dataSnapshot -> {
            studentName = dataSnapshot.child("name").getValue(String.class);
            studentEmail = dataSnapshot.child("email").getValue(String.class);
            tvStudentName.setText(studentName);
            tvEmail.setText(studentEmail);
        });
    }

    private void submitFeedback() {
        String improvement = etImprovement.getText().toString();
        String comments = etComments.getText().toString();

        if (selectedSatisfaction == null || selectedRecommendation == null) {
            Toast.makeText(this, "Please select satisfaction and recommendation options", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique feedbackId
        String feedbackId = feedbackRef.push().getKey();

        if (feedbackId != null) {
            Feedback feedback = new Feedback(feedbackId, userId, studentName, studentEmail, selectedSatisfaction, improvement, comments, selectedRecommendation);

            // **Fix: Save feedback under `feedback/students/feedbackId`**
            feedbackRef.child(feedbackId).setValue(feedback)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show());
        }
    }
}
