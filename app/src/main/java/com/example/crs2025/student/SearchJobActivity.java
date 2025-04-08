package com.example.crs2025.student;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchJobActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView lvJobs;
    private Button btnApplyJob, btnGoBack;
    private DatabaseReference jobsRef;
    private List<String> jobList;
    private ArrayAdapter<String> jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);

        etSearch = findViewById(R.id.et_search);
        lvJobs = findViewById(R.id.lv_jobs);
        btnApplyJob = findViewById(R.id.btn_apply_job);
        btnGoBack = findViewById(R.id.btn_go_back);

        jobsRef = FirebaseDatabase.getInstance().getReference("globalJobs");
        jobList = new ArrayList<>();
        jobAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobList);
        lvJobs.setAdapter(jobAdapter);

        fetchJobs();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                jobAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnApplyJob.setOnClickListener(v -> startActivity(new Intent(SearchJobActivity.this, ApplyJobActivity.class)));
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchJobs() {
        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    String jobTitle = jobSnap.child("jobTitle").getValue(String.class);
                    String companyName = jobSnap.child("companyName").getValue(String.class);

                    if (jobTitle != null && companyName != null) {
                        String jobDisplay = jobTitle + " - " + companyName;
                        if (!jobList.contains(jobDisplay)) {
                            jobList.add(jobDisplay);
                        }
                    }
                }
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}