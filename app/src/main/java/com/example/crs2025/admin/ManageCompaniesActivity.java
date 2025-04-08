package com.example.crs2025.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crs2025.R;
import com.example.crs2025.adapters.CompanyAdapter;
import com.example.crs2025.models.Company;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageCompaniesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCompanies;
    private Button btnDeleteSelected, btnGoBack;
    private List<Company> companyList;
    private CompanyAdapter adapter;
    private DatabaseReference companiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_companies);

        recyclerViewCompanies = findViewById(R.id.recycler_companies);
        btnDeleteSelected = findViewById(R.id.btn_delete_selected);
        btnGoBack = findViewById(R.id.btn_go_back);

        recyclerViewCompanies.setLayoutManager(new LinearLayoutManager(this));
        companyList = new ArrayList<>();
        adapter = new CompanyAdapter(this, companyList);
        recyclerViewCompanies.setAdapter(adapter);

        companiesRef = FirebaseDatabase.getInstance().getReference("users").child("companies");

        fetchCompanies();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedCompanies());
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchCompanies() {
        companiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                companyList.clear();
                for (DataSnapshot companySnap : snapshot.getChildren()) {
                    Company company = companySnap.getValue(Company.class);
                    if (company != null) {
                        companyList.add(company);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageCompaniesActivity.this, "Failed to load companies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedCompanies() {
        List<Company> selectedCompanies = adapter.getSelectedCompanies();

        if (selectedCompanies.isEmpty()) {
            Toast.makeText(this, "No companies selected for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete the selected companies?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();

                    for (Company company : selectedCompanies) {
                        // Instead of deleting, mark as "deleted" in the database
                        Task<Void> markAsDeleted = companiesRef.child(company.getId()).removeValue();
                        deleteTasks.add(markAsDeleted);
                    }

                    // Wait for all tasks to complete before refreshing the list
                    Tasks.whenAllComplete(deleteTasks)
                            .addOnCompleteListener(task -> {
                                adapter.clearSelection();
                                fetchCompanies();
                                Toast.makeText(this, "Selected companies deleted successfully", Toast.LENGTH_SHORT).show();
                            });

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
