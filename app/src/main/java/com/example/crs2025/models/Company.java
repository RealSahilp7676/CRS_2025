package com.example.crs2025.models;

public class Company {
    private String id;
    private String name;
    private String email;
    private String address;
    private String role;
    private String password; // Stored in DB but not used in UI

    // Default constructor required for Firebase
    public Company() {
    }

    public Company(String id, String name, String email, String address, String role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
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

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
