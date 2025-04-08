package com.example.crs2025.models;

public class Application {
    private String applicationId;
    private String studentId;
    private String jobId;
    private String companyId;
    private String jobTitle;
    private String companyName;
    private String skills;
    private String fullName;
    private String email;
    private String address;
    private String branch;
    private String cgpa;
    private String reasonToApply;
    private String resumeLink;
    private String status; // Pending, Approved, Rejected

    public Application() {
        // Default constructor required for Firebase
    }

    public Application(String applicationId, String studentId, String jobId, String companyId, String jobTitle,
                       String companyName, String skills, String fullName, String email, String address,
                       String branch, String cgpa, String reasonToApply, String resumeLink, String status) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.jobId = jobId;
        this.companyId = companyId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.skills = skills;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.branch = branch;
        this.cgpa = cgpa;
        this.reasonToApply = reasonToApply;
        this.resumeLink = resumeLink;
        this.status = status;
    }

    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getCgpa() { return cgpa; }
    public void setCgpa(String cgpa) { this.cgpa = cgpa; }

    public String getReasonToApply() { return reasonToApply; }
    public void setReasonToApply(String reasonToApply) { this.reasonToApply = reasonToApply; }

    public String getResumeLink() { return resumeLink; }
    public void setResumeLink(String resumeLink) { this.resumeLink = resumeLink; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
