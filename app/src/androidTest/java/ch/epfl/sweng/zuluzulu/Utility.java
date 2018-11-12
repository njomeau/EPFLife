package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Admin;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Guest;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.UserRole;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Use this class for functions that are used in multiple tests
 */
public class Utility {

    /**
     * Create a guest user
     * @return Return a Guest
     */
    public static Guest createTestGuest(){
        return (new User.UserBuilder()).buildGuestUser();
    }

    /**
     * Create a user for the tests
     *
     * @return Return an AithenticatedUser
     */
    public static AuthenticatedUser createTestAuthenticated() {
        User.UserBuilder builder = createFilledUserBuilder();
        User user = builder.buildAuthenticatedUser();
        assert (user != null);
        assert (user.isConnected());

        return (AuthenticatedUser)user;
    }

    /**
     * Create a admin for the tests
     *
     * @return Return an Admin
     */
    public static Admin createTestAdmin() {
        User.UserBuilder builder = createFilledUserBuilder();
        User user = builder.buildAdmin();
        assert (user != null);
        assert (user.hasRole(UserRole.ADMIN));

        return (Admin)user;
    }

    /**
     * Add user to main
     * <p>
     * !!! TO READ !!!
     *
     * @param mActivityRule Activity rule
     * @param user          User
     * @warning NEED TO BE CALLED TO CREATE THE ACTIVITY
     * USE IN RULE : new ActivityTestRule<>(MainActivity.class, false, false);
     * <p>
     * It's allow us to not start the Activity before !
     * <p>
     * !!! TO READ !!!
     */
    public static void addUserToMainIntent(ActivityTestRule<MainActivity> mActivityRule, User user) {
        // Put the user into the main
        Intent i = new Intent();
        i.putExtra("user", user);
        mActivityRule.launchActivity(i);
    }

    /**
     * Open the menu from the mainActivity
     */
    public static void openMenu() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
    }

    /**
     * Close the menu from the mainActivity
     */
    public static void closeMenu() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)))
                .perform(DrawerActions.close());
    }

    /**
     * Check if the fragment is open
     *
     * @param id fragment id
     */
    public static void checkFragmentIsOpen(int id) {
        onView(withId(id)).check(matches(isDisplayed()));
    }

    /**
     * Check if the fragment is not open
     *
     * @param id fragment id
     */
    public static void checkFragmentIsClosed(int id) {
        onView(withId(id)).check(doesNotExist());
    }

    /**
     * Return a userbuilder already filled with all the user informations
     *
     * @return UserBuilder
     */
    private static User.UserBuilder createFilledUserBuilder() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("123456");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setSection("IN");
        builder.setSemester("BA5");
        builder.setFirst_names("James");
        builder.setLast_names("Bond");
        builder.setFavAssos(Arrays.asList(2));
        builder.setFollowedEvents(Arrays.asList(1, 2));
        builder.setFollowedChats(new ArrayList<Integer>());

        return builder;
    }

    /**
     * Return a default channel
     *
     * @return a default channel
     */
    public static Channel defaultChannel() {
        Map data = new HashMap();
        data.put("id", 1L);
        data.put("name", "name");
        data.put("description", "description");
        data.put("restrictions", new HashMap<>());
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(data);
        return new Channel(fmap);
    }

}
