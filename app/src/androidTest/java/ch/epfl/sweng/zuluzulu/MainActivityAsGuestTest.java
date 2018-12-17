package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * This class test the MainActivity as a Guest User
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityAsGuestTest extends TestWithGuestAndFragment<MainFragment> {

    @Override
    public void initFragment() {
 android.util.Log.d("Function called", "initFragment");
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void isAuthenticated() {
 android.util.Log.d("Function called", "isAuthenticated");
        // check not authenticated
        assertFalse(getMainActivity().isAuthenticated());
    }

    @Test
    public void onBackPressedWorks(){
        Utility.openMenu();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_about));
        Utility.checkFragmentIsOpen(R.id.about_fragment);
        Espresso.pressBack();
        Utility.checkFragmentIsOpen(R.id.main_fragment);
    }

    @Test
    public void getUser() {
 android.util.Log.d("Function called", "getUser");
        assertNotNull(getMainActivity().getUser());
    }

}