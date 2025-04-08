package com.example.crs2025.models;

public class User {
    private String id;
    private String name;         // Full Name (Student) / Company Name (Company)
    private String email;
    private String address;      // Address (For both)
    private String role;         // "Student" or "Company"

    // Additional fields for Students
    private String enrollmentNo;
    private String branch;
    private String institute;
    private String password;

    // Constructor for Student
    public User(String id, String name, String email, String address, String role, String enrollmentNo, String branch, String institute, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.enrollmentNo = enrollmentNo;
        this.branch = branch;
        this.institute = institute;
        this.password = password;
        this.role = role;
    }

    // Constructor for Company
    public User(String id, String name, String email, String address, String role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.password = password;
        this.enrollmentNo = "";
        this.branch = "";
        this.institute = "";
    }

    // Default constructor (needed for Firebase)
    public User() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEnrollmentNo() { return enrollmentNo; }
    public void setEnrollmentNo(String enrollmentNo) { this.enrollmentNo = enrollmentNo; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getInstitute() { return institute; }
    public void setInstitute(String institute) { this.institute = institute; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

