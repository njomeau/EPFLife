package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperChatPostsFragment;
import ch.epfl.sweng.zuluzulu.structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest extends TestWithAuthenticatedAndFragment<ChatFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = ChatFragment.newInstance(user, Utility.defaultChannel());
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

    @Test
    public void testUserCanVisitOtherProfileByClickingOnChatMessage() {
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
        onData(instanceOf(ChatMessage.class)).atPosition(1).check(matches(isDisplayed()));
        onData(instanceOf(ChatMessage.class)).atPosition(1).perform(ViewActions.longClick());
        onView(withText(startsWith(SuperChatPostsFragment.VISIT_PROFILE_STRING))).check(matches(isDisplayed()));
        onView(withText("Oui")).perform(click());
        Utility.checkFragmentIsOpen(R.id.profile_fragment);
    }

    @Test
    public void testUserCannotReachOwnProfile() {
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
        onData(instanceOf(ChatMessage.class)).atPosition(0).check(matches(isDisplayed()));
        onData(instanceOf(ChatMessage.class)).atPosition(0).perform(ViewActions.longClick());
        onView(withText(startsWith(SuperChatPostsFragment.VISIT_PROFILE_STRING))).check(doesNotExist());
    }
}
