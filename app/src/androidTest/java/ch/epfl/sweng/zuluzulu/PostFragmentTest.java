package ch.epfl.sweng.zuluzulu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Fragments.PostFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class PostFragmentTest extends TestWithLogin {

    @Before
    public void init() {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(Utils.defaultChannelData());
        Channel channel = new Channel(fmap);
        SuperFragment fragment = PostFragment.newInstance(getUser(), channel);
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void testUserCanSeePosts() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
    }
}