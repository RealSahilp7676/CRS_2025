package com.example.crs2025.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crs2025.R;
import com.example.crs2025.models.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    private Context context;
    private List<Company> companyList;
    private List<Company> selectedCompanies = new ArrayList<>();

    public CompanyAdapter(Context context, List<Company> companyList) {
        this.context = context;
        this.companyList = companyList;
    }

    public List<Company> getSelectedCompanies() {
        return selectedCompanies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_company, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Company company = companyList.get(position);

        holder.tvCompanyName.setText("Company: " + company.getName());
        holder.tvEmail.setText("Email: " + company.getEmail());
        holder.tvAddress.setText("Address: " + company.getAddress());

        // 🔹 Detach previous listener to avoid incorrect checkbox movement
        holder.checkBox.setOnCheckedChangeListener(null);

        // 🔹 Set checkbox state correctly
        holder.checkBox.setChecked(selectedCompanies.contains(company));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCompanies.add(company);
            } else {
                selectedCompanies.remove(company);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCompanyName, tvEmail, tvAddress;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompanyName = itemView.findViewById(R.id.tv_company_name);
            tvEmail = itemView.findViewById(R.id.tv_company_email);
            tvAddress = itemView.findViewById(R.id.tv_company_address);
            checkBox = itemView.findViewById(R.id.checkbox_select);
        }
    }
    public void clearSelection() {
        selectedCompanies.clear();
        notifyDataSetChanged(); // Refresh UI and reset checkboxes
    }

}
