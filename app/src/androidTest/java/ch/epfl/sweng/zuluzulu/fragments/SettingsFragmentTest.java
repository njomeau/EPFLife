package ch.epfl.sweng.zuluzulu.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest extends TestWithGuestAndFragment<SettingsFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = SettingsFragment.newInstance();
    }

    @Test
    public void clickOnNightLightSwitchBis() {
        onView(ViewMatchers.withId(R.id.switch_night_light))
                .perform(click());

    }

    @Test
    public void clickOnNotificationSwitchTest() {
        onView(withId(R.id.switch_notifications))
                .perform(click());
    }

    @Test
    public void clickOnClearButtonTest() {
        onView(withId(R.id.button_clear_cache))
                .perform(click());
    }

    @Test
    public void testClickOnAnonymous() {
        SharedPreferences preferences = getMainActivity().getPreferences(Context.MODE_PRIVATE);
        boolean anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        onView(withId(R.id.switch_notifications)).perform(click());

        onView(withId(R.id.switch_chat_anonym)).perform(click());

        boolean anonymousAfterClick = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);
        assertNotEquals(anonymousAfterClick, anonymous);

        onView(withId(R.id.switch_chat_anonym)).perform(click());
        boolean anonymousAfterTwoClick = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);
        assertEquals(anonymousAfterTwoClick, anonymous);
    }

}
