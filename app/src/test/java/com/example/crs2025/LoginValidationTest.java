package com.example.crs2025;
import static org.junit.Assert.*;

import com.example.crs2025.utils.LoginUtils;

import org.junit.Test;

    public class LoginValidationTest {

        @Test
        public void validLoginTest() {
            boolean result = LoginUtils.validateCredentials("student@gmail.com", "Password123");
            assertTrue(result);
        }

        @Test
        public void invalidLoginTest() {
            boolean result = LoginUtils.validateCredentials("wrong@gmail.com", "wrongpass");
            assertFalse(result);
        }

        @Test
        public void emptyFieldsTest() {
            boolean result = LoginUtils.validateCredentials("", "");
            assertFalse(result);
        }
    }
