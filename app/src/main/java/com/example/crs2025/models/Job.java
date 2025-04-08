package com.example.crs2025.models;

public class Job {
    private String jobId;
    private String companyId;
    private String companyName;  // New field
    private String jobTitle;
    private String skills;
    private String cgpa;
    private String jobType;
    private boolean internshipRequired;

    public Job() {
        // Default constructor required for Firebase
    }

    public Job(String jobId, String companyId, String companyName, String jobTitle, String skills, String cgpa, String jobType, boolean internshipRequired) {
        this.jobId = jobId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.skills = skills;
        this.cgpa = cgpa;
        this.jobType = jobType;
        this.internshipRequired = internshipRequired;
    }

    // Getters and setters
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public boolean isInternshipRequired() {
        return internshipRequired;
    }

    public void setInternshipRequired(boolean internshipRequired) {
        this.internshipRequired = internshipRequired;
    }
}
