package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest {

    private static final String MSG = "HELLO FROM ZULUZULU";
    private static final String CHANNEL = "Test";
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private User user;
    private Fragment fragment;

    @Before
    public void init() {
        user = Utility.createTestUser();

        fragment = ChatFragment.newInstance(user, 1);
        mActivityRule.getActivity().openFragment(fragment);
    }


    @Test
    public void testUserCanSendAMessageAndReadIt() throws InterruptedException {
        /* Todo
        I don't understand how to test this without the dependence of firestore
        I don't want to write a message in the database during the test
        How can I mock the behavior of firestore..?
         */

        // Type message
        onView(withId(R.id.chat_message_edit)).perform(replaceText(MSG));

        onView(withId(R.id.chat_send_button)).perform(click());
    }
}
