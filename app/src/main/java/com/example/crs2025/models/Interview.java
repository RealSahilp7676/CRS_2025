package com.example.crs2025.models;

public class Interview {
    private String interviewId;
    private String companyId;
    private String companyName;
    private String jobTitle;
    private String studentId;
    private String studentName;
    private String date;
    private String time;
    private String venue;
    private String interviewType;

    public Interview() {
        // Default constructor required for Firebase
    }

    public Interview(String interviewId, String companyId, String companyName, String jobTitle,
                     String studentId, String studentName, String date, String time,
                     String venue, String interviewType) {
        this.interviewId = interviewId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.interviewType = interviewType;
    }

    // Getters and Setters
    public String getInterviewId() { return interviewId; }
    public void setInterviewId(String interviewId) { this.interviewId = interviewId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getInterviewType() { return interviewType; }
    public void setInterviewType(String interviewType) { this.interviewType = interviewType; }
}
