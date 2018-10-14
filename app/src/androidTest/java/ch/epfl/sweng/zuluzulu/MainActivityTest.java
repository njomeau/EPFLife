package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Structure.Guest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private MainActivity mActivity;


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void onCreateGoesToMain() {
    }

    @Test
    public void onOptionsItemSelected() {
    }

    @Test
    public void isAuthenticated() {
        assertEquals(mActivity.isAuthenticated(), mActivity.getUser().isConnected());
        // Check it means it's not an user
        assertEquals(!mActivity.isAuthenticated(), mActivity.getUser() instanceof Guest);
    }

    @Test
    public void onFragmentInteraction() {
    }

    @Test
    public void getUser() {
        assertNotNull(mActivity.getUser());
    }
}