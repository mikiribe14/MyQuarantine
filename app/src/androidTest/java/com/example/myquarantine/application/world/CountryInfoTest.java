package com.example.myquarantine.application.world;


import android.widget.Spinner;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.myquarantine.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CountryInfoTest {

    @Rule
    public ActivityTestRule<WorldActivity> mActivityTestRule = new ActivityTestRule<>(WorldActivity.class);

    @Test
    public void countryInfoTest() {

        onView(withId(R.id.spinner)).perform(click());

        onData(allOf(is((String.class)), is("Spain")))
                .perform(click());

        Spinner spinner = (Spinner) mActivityTestRule.getActivity().findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString();

        assertEquals("Spain", text);

    }

    @Test
    public void countryInfoTest2() {

        onView(withId(R.id.spinner)).perform(click());

        onData(allOf(is((String.class)), is("Denmark")))
                .perform(click());

        Spinner spinner = (Spinner) mActivityTestRule.getActivity().findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString();

        assertEquals("Denmark", text);

    }


}

