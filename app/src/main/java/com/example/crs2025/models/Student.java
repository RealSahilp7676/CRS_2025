package com.example.crs2025.models;

public class Student {
    private String id;
    private String name;
    private String email;
    private String address;
    private String branch;
    private String enrollmentNo;
    private String institute;
    private String role;
    private String password; // Not used in UI, but stored in DB

    // Default constructor required for Firebase
    public Student() {
    }

    public Student(String id, String name, String email, String address, String branch, String enrollmentNo, String institute, String role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.branch = branch;
        this.enrollmentNo = enrollmentNo;
        this.institute = institute;
        this.role = role;
        this.password = password;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getEnrollmentNo() { return enrollmentNo; }
    public void setEnrollmentNo(String enrollmentNo) { this.enrollmentNo = enrollmentNo; }

    public String getInstitute() { return institute; }
    public void setInstitute(String institute) { this.institute = institute; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
