package com.example.crs2025.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Interview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrackInterviewActivity extends AppCompatActivity {

    private ListView lvInterviews;
    private TextView tvNoInterviews;
    private Button btnGoBack;
    private DatabaseReference interviewsRef;
    private FirebaseAuth mAuth;
    private List<Interview> interviewList;
    private ArrayAdapter<String> adapter;
    private List<String> interviewDisplayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_interview);

        // Initialize views
        lvInterviews = findViewById(R.id.lv_interviews);
        tvNoInterviews = findViewById(R.id.tv_no_interviews);
        btnGoBack = findViewById(R.id.btn_go_back);

        mAuth = FirebaseAuth.getInstance();
        String studentId = mAuth.getCurrentUser().getUid();
        interviewsRef = FirebaseDatabase.getInstance().getReference("globalInterviews");

        interviewList = new ArrayList<>();
        interviewDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, interviewDisplayList);
        lvInterviews.setAdapter(adapter);

        fetchInterviews(studentId);

        lvInterviews.setOnItemClickListener((parent, view, position, id) -> {
            Interview selectedInterview = interviewList.get(position);
            Intent intent = new Intent(TrackInterviewActivity.this, InterviewDetailsForStudentActivity.class);
            intent.putExtra("interviewId", selectedInterview.getInterviewId());
            startActivity(intent);
        });
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchInterviews(String studentId) {
        interviewsRef.orderByChild("studentId").equalTo(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                interviewList.clear();
                interviewDisplayList.clear();

                if (!snapshot.exists()) {
                    showNoInterviewsMessage();
                    return;
                }

                hideNoInterviewsMessage();

                for (DataSnapshot interviewSnap : snapshot.getChildren()) {
                    Interview interview = interviewSnap.getValue(Interview.class);
                    if (interview != null) {
                        interviewList.add(interview);
                        interviewDisplayList.add(interview.getCompanyName() + " - " + interview.getJobTitle() + " (" + interview.getDate() + ")");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrackInterviewActivity.this, "Failed to fetch interviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNoInterviewsMessage() {
        tvNoInterviews.setVisibility(View.VISIBLE);
        lvInterviews.setVisibility(View.GONE);
    }

    private void hideNoInterviewsMessage() {
        tvNoInterviews.setVisibility(View.GONE);
        lvInterviews.setVisibility(View.VISIBLE);
    }
}
