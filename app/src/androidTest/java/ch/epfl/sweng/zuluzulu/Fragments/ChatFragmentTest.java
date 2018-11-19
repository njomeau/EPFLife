package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest extends TestWithAuthenticatedAndFragment<ChatFragment> {

    @Override
    public void initFragment() {
        fragment = ChatFragment.newInstance(user, Utility.defaultChannel());
        DatabaseFactory.setDependency(new FirebaseMock());
    }

    @Test
    public void testUserCanSeeMessages() {
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanWriteInTextEditBox() {
        onView(withId(R.id.chat_message_edit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
    }

    @Test
    public void testSendButtonIsEnabledOnlyIfMessageNotEmpty() {
        onView(withId(R.id.chat_send_button)).check(matches(not(isEnabled())));
        onView(withId(R.id.chat_message_edit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.chat_send_button)).check(matches(isEnabled()));
    }

    @Test
    public void testPostButtonIsEnabled() {
        onView(withId(R.id.posts_button)).check(matches(isEnabled()));
        onView(withId(R.id.chat_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testUserCanGoToPosts() {
        onView(withId(R.id.posts_button)).perform(ViewActions.click());
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanSendMessage() {
        onView(withId(R.id.chat_message_edit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.chat_send_button)).perform(ViewActions.click());
    }
}
