package com.example.myquarantine.application.home;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.myquarantine.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AudioButtonsTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void audioButtonsTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        floatingActionButton.perform(click());

        onView(withId(R.id.startButton)).check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startButton), withText("START"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                9),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.audioText), withText("Recording..."),
                        childAtPosition(
                                allOf(withId(R.id.audioHolder),
                                        childAtPosition(
                                                withId(R.id.editNoteLayout),
                                                7)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Recording...")));

        onView(withId(R.id.stopButton)).check(matches(isDisplayed()));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.stopButton), withText("STOP "),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                12),
                        isDisplayed()));
        appCompatButton4.perform(click());

        onView(withId(R.id.playButton)).check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.audioText), withText("Your audio"),
                        childAtPosition(
                                allOf(withId(R.id.audioHolder),
                                        childAtPosition(
                                                withId(R.id.editNoteLayout),
                                                7)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Your audio")));

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.playButton), withText("PLAY"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                10),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.audioText), withText("Playing..."),
                        childAtPosition(
                                allOf(withId(R.id.audioHolder),
                                        childAtPosition(
                                                withId(R.id.editNoteLayout),
                                                7)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Playing...")));

        onView(withId(R.id.pauseButton)).check(matches(isDisplayed()));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.pauseButton), withText("STOP"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()));
        appCompatButton6.perform(click());

        onView(withId(R.id.startButton)).check(matches(isDisplayed()));
        onView(withId(R.id.playButton)).check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
