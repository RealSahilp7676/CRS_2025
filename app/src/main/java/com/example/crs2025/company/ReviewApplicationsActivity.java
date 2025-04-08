package com.example.crs2025.company;

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

public class ReviewApplicationsActivity extends AppCompatActivity {

    private ListView lvApplications;
    private TextView tv_no_applications;
    private Button btn_go_back;
    private DatabaseReference applicationsRef;
    private FirebaseAuth mAuth;
    private List<Application> applicationList;
    private List<String> applicationDisplayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_applications);

        lvApplications = findViewById(R.id.lv_applications);
        tv_no_applications = findViewById(R.id.tv_no_applications);
        btn_go_back = findViewById(R.id.btn_go_back);

        mAuth = FirebaseAuth.getInstance();
        String companyId = mAuth.getCurrentUser().getUid();

        applicationsRef = FirebaseDatabase.getInstance().getReference("applications").child(companyId);
        applicationList = new ArrayList<>();
        applicationDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, applicationDisplayList);
        lvApplications.setAdapter(adapter);

        fetchApplications();

        lvApplications.setOnItemClickListener((parent, view, position, id) -> {
            Application selectedApplication = applicationList.get(position);
            Intent intent = new Intent(ReviewApplicationsActivity.this, ApplicationDetailsForCompanyActivity.class);
            intent.putExtra("applicationId", selectedApplication.getApplicationId());
            intent.putExtra("companyId", companyId);
            startActivity(intent);
        });

        btn_go_back.setOnClickListener(v -> finish());
    }

    private void fetchApplications() {
        applicationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList.clear();
                applicationDisplayList.clear();
                for (DataSnapshot appSnap : snapshot.getChildren()) {
                    Application application = appSnap.getValue(Application.class);
                    if (application != null) {
                        applicationList.add(application);
                        applicationDisplayList.add(application.getJobTitle() + " - " + application.getAddress());
                    }
                }

                if (applicationList.isEmpty()) {
                    tv_no_applications.setVisibility(View.VISIBLE); // Show "No Applications" message
                    lvApplications.setVisibility(View.GONE); // Hide the ListView
                } else {
                    tv_no_applications.setVisibility(View.GONE); // Hide "No Applications" message
                    lvApplications.setVisibility(View.VISIBLE); // Show the ListView
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewApplicationsActivity.this, "Failed to fetch applications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
