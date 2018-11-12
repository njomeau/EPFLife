package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest extends TestWithLogin {

    @Before
    public void init() {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(Utils.defaultChannelData());
        Channel channel = new Channel(fmap);
        SuperFragment fragment = ChatFragment.newInstance(getUser(), channel);
        mActivityRule.getActivity().openFragment(fragment);
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
}
