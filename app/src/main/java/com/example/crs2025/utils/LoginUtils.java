package com.example.crs2025.utils;

public class LoginUtils {

    public static boolean validateCredentials(String email, String password) {
        // Example: Simple validation (Modify as per your actual validation logic)
        if (email.isEmpty() || password.isEmpty()) {
            return false; // Empty fields are invalid
        }

        // Assume valid email and password for testing
        return email.equals("student@gmail.com") && password.equals("Password123");
    }
}
