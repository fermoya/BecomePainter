package com.example.fmoyader.becomepainter;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.fmoyader.becomepainter.activities.CanvasDrawerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by fmoyader on 6/6/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CanvasActivityBehaviourTest {

    @Rule
    public ActivityTestRule<CanvasDrawerActivity> rule
            = new ActivityTestRule<>(CanvasDrawerActivity.class);

    @Test
    public void saveAndDeletePainting(){
        onView(withId(R.id.canvas)).perform(swipeRight(), swipeUp());

        onView(withId(R.id.button_save)).perform(click());

        onView(withText(R.string.dialog_save_text)).check(matches(isDisplayed()));

        onView(withId(R.id.edit_text_painting_title)).perform(typeText("Test Title"));
        onView(withId(R.id.edit_text_painting_author)).perform(typeText("Test Author"));
        onView(withId(R.id.edit_text_painting_description))
                .perform(typeText("Test Description"), closeSoftKeyboard());

        onView(withText(R.string.dialog_save_text)).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText(R.string.drawer_item_my_drawings)).perform(click());

        onView(withText("Test Title"))
                .check(matches(isDisplayed()))
                .perform(swipeRight())
                .check(doesNotExist());
    }

}
