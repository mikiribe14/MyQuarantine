package com.example.myquarantine.application.home;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.myquarantine.R;
import com.example.myquarantine.application.settings.SettingsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateViewEditNoteTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void createFirstNoteTest() {
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

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button2), withText("SAVE IT!"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatButton.perform(click());
/*
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.noteHolder),
                        childAtPosition(
                                allOf(withId(R.id.rv),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                0)),
                                0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.noteHolder),
                        childAtPosition(
                                allOf(withId(R.id.rv),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                1)),
                                0),
                        isDisplayed()));
        linearLayout2.perform(longClick());



        linearLayout = onView(
                allOf(withId(R.id.noteHolder),
                        childAtPosition(
                                allOf(withId(R.id.rv),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                0)),
                                0),
                        isDisplayed()));
        linearLayout.check(doesNotExist());

*/
    }

    @Test
    public void viewFirstNoteTest() {
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

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.titleField),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("title"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.descriptionField),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("desc"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button2), withText("SAVE IT!"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.noteHolder),
                        childAtPosition(
                                allOf(withId(R.id.rv),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                1)),
                                0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.titleText), withText("title"),
                        childAtPosition(
                                allOf(withId(R.id.viewNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("title")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.descriptionText), withText("desc"),
                        childAtPosition(
                                allOf(withId(R.id.viewNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        textView2.check(matches(withText("desc")));

    }

    @Test
    public void editNoteTest() {
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

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.titleField),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("title"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.descriptionField),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("desc"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button2), withText("SAVE IT!"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.noteHolder),
                        childAtPosition(
                                allOf(withId(R.id.rv),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                1)),
                                0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.titleText), withText("title"),
                        childAtPosition(
                                allOf(withId(R.id.viewNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("title")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.descriptionText), withText("desc"),
                        childAtPosition(
                                allOf(withId(R.id.viewNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        textView2.check(matches(withText("desc")));



        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.editButton), withText("EDIT"),
                        childAtPosition(
                                allOf(withId(R.id.viewNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.descriptionField), withText("desc"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("description"));



        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.descriptionField), withText("description"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.button2), withText("SAVE IT!"),
                        childAtPosition(
                                allOf(withId(R.id.editNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.noteHolder),
                        childAtPosition(
                                allOf(withId(R.id.rv),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                1)),
                                0),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.descriptionText), withText("description"),
                        childAtPosition(
                                allOf(withId(R.id.viewNoteLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText("description")));
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
