package com.example.crs2025;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.crs2025.activities.LoginActivity;
import com.example.crs2025.dashboards.StudentDashboardActivity;
import com.example.crs2025.student.ApplyJobActivity;
import com.example.crs2025.utils.ElapsedTimeIdlingResource;
import com.example.crs2025.R;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ApplyJobUITest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void setUp() {
        // Initialize Espresso Intents
        Intents.init();

        // Increase wait time for UI transitions (3 seconds)
        idlingResource = new ElapsedTimeIdlingResource(3000);
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void applyJobTest() {
        // Change Spinner selection from Admin to Student
        Espresso.onView(ViewMatchers.withId(R.id.spinner_role))
                .perform(ViewActions.click());
        Espresso.onData(Matchers.anything())
                .atPosition(1) // Assuming "Student" is the second item (index 1)
                .perform(ViewActions.click());

        // Enter login credentials and close the keyboard
        Espresso.onView(ViewMatchers.withId(R.id.et_email))
                .perform(ViewActions.typeText("sahil@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.et_password))
                .perform(ViewActions.typeText("sahil123"), ViewActions.closeSoftKeyboard());

        // Click on the Login button
        Espresso.onView(ViewMatchers.withId(R.id.btn_login))
                .perform(ViewActions.click());

        // Wait a moment for the StudentDashboardActivity to load
        try {
            Thread.sleep(2000); // Temporary delay for UI transition
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that StudentDashboardActivity has been launched
        Intents.intended(hasComponent(StudentDashboardActivity.class.getName()));

        // Check that the student dashboard layout is displayed
        Espresso.onView(ViewMatchers.withId(R.id.student_dashboard_layout))
                .check(matches(isDisplayed()));

        // Click the "Apply Job" button
        // IMPORTANT: Use the actual resource ID (e.g., R.id.btnApplyJob if defined in XML as @+id/btnApplyJob)
        Espresso.onView(ViewMatchers.withId(R.id.btnApplyJob))
                .perform(ViewActions.click());

        // Verify that ApplyJobActivity is launched
        Intents.intended(hasComponent(ApplyJobActivity.class.getName()));

        // Optionally check that the apply job layout is displayed
        Espresso.onView(ViewMatchers.withId(R.id.apply_job_layout))
                .check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        // Release Espresso Intents and unregister the IdlingResource
        Intents.release();
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
}
    }
}
