package com.example.crs2025.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crs2025.R;
import com.example.crs2025.models.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrackApplicationsActivity extends AppCompatActivity {

    private ListView lvApplications;
    private TextView tvNoApplications;
    private Button btnGoBack;
    private DatabaseReference applicationsRef;
    private FirebaseAuth mAuth;
    private List<Application> applicationList;
    private ArrayAdapter<String> adapter;
    private List<String> applicationDisplayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_applications);

        lvApplications = findViewById(R.id.lv_applications);
        tvNoApplications = findViewById(R.id.tv_no_applications);
        btnGoBack = findViewById(R.id.btn_go_back);

        mAuth = FirebaseAuth.getInstance();
        applicationsRef = FirebaseDatabase.getInstance().getReference("globalApplications");

        applicationList = new ArrayList<>();
        applicationDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, applicationDisplayList);
        lvApplications.setAdapter(adapter);

        fetchApplications();


        lvApplications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Application selectedApplication = applicationList.get(position);
                Intent intent = new Intent(TrackApplicationsActivity.this, ApplicationDetailsActivity.class);
                intent.putExtra("applicationId", selectedApplication.getApplicationId());
                startActivity(intent);
            }
        });

        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchApplications() {
        String studentId = mAuth.getCurrentUser().getUid();
        applicationsRef.orderByChild("studentId").equalTo(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList.clear();
                applicationDisplayList.clear();
                for (DataSnapshot appSnap : snapshot.getChildren()) {
                    Application application = appSnap.getValue(Application.class);
                    if (application != null) {
                        applicationList.add(application);
                        applicationDisplayList.add(application.getJobTitle() + " - " + application.getCompanyName() + " (" + application.getStatus() + ")");
                    }
                }

                if (applicationList.isEmpty()) {
                    tvNoApplications.setVisibility(View.VISIBLE); // Show "No Applications" message
                    lvApplications.setVisibility(View.GONE); // Hide ListView
                } else {
                    tvNoApplications.setVisibility(View.GONE); // Hide "No Applications" message
                    lvApplications.setVisibility(View.VISIBLE); // Show ListView
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrackApplicationsActivity.this, "Failed to fetch applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
