package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MenuAsGuestTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void openDrawer() {
        DatabaseFactory.setDependency(new MockedProxy());

        // Open the menu
        Utility.openMenu();
    }

    @Test
    public void testGuestUserDoesNotSeeEveryOption() {
        onView(withText("Chat")).check(doesNotExist());
        onView(withText("Logout")).check(doesNotExist());
        onView(withText("Profile")).check(doesNotExist());
    }

    @Test
    public void testGuestCanOpenMainFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_main));


        Utility.checkFragmentIsOpen(R.id.main_fragment);
    }

    @Test
    public void testGuestCanOpenSettingsFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));


        Utility.checkFragmentIsOpen(R.id.settings_fragment);
    }

    @Test
    public void testGuestCanOpenLoginFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_login));


        Utility.checkFragmentIsOpen(R.id.login_fragment);
    }

    @Test
    public void testCanOpenAboutFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_about));

        Utility.checkFragmentIsOpen(R.id.about_fragment);
    }
}