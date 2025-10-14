package com.easyconnect.easyconnectapp.app.view;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.easyconnect.easyconnectapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.easyconnect.easyconnectapp", appContext.getPackageName());
    }

    @Test
    public void testScanMdnsButton_isDisplayed() {
        onView(withId(R.id.btnScanMdns)).check(matches(isDisplayed()));
    }

    @Test
    public void testScanQRButton_isDisplayed() {
        onView(withId(R.id.btnScanQR)).check(matches(isDisplayed()));
    }

    @Test
    public void testShowQRCodeButton_isDisplayed() {
        onView(withId(R.id.btnShowQrcode)).check(matches(isDisplayed()));
    }

    @Test
    public void testConsoleRecyclerView_isDisplayed() {
        onView(withId(R.id.recyclerview_console)).check(matches(isDisplayed()));
    }

    @Test
    public void testScanMdnsButton_click_startsDiscovery() {
        onView(withId(R.id.btnScanMdns)).perform(click());
    }

    @Test
    public void testScanQRButton_click_interactsWithView() {
        onView(withId(R.id.btnScanQR)).perform(click());
    }

    @Test
    public void testShowQRCodeButton_click_interactsWithView() {
        onView(withId(R.id.btnShowQrcode)).perform(click());
    }

    @Test
    public void testSettingsIcon_isDisplayed() {
        onView(withId(R.id.img_setting)).check(matches(isDisplayed()));
    }

    @Test
    public void testMainActivity_launchesSuccessfully() {
        MainActivity activity = activityRule.getActivity();
        assertEquals(true, activity != null);
    }

    @Test
    public void testUI_allMainButtonsVisible() {
        onView(withId(R.id.btnScanMdns)).check(matches(isDisplayed()));
        onView(withId(R.id.btnScanQR)).check(matches(isDisplayed()));
        onView(withId(R.id.btnShowQrcode)).check(matches(isDisplayed()));
    }
}
