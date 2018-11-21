package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.User;

/**
 * A {@link SuperChatPostsFragment} subclass.
 * This fragment is used to display the posts
 */
public class PostFragment extends SuperChatPostsFragment {
    private static final String TAG = "POST_TAG";

    private static final String POSTS_COLLECTION_NAME = "posts";

    private List<Post> posts = new ArrayList<>();
    private PostArrayAdapter adapter;

    private Button writePostButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(User user, Channel channel) {
        return (PostFragment) newInstanceOf("post", user, channel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        listView = view.findViewById(R.id.posts_list_view);
        chatButton = view.findViewById(R.id.chat_button);
        postsButton = view.findViewById(R.id.posts_button);
        writePostButton = view.findViewById(R.id.posts_new_post_button);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_post);

        chatButton.setEnabled(true);
        postsButton.setEnabled(false);

        adapter = new PostArrayAdapter(view.getContext(), posts);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        updatePosts();
        setUpChatButton();
        setUpNewPostButton();

        return view;
    }

    /**
     * Add an onClick listener on the button to switch to the chat fragment
     */
    private void setUpChatButton() {
        chatButton.setOnClickListener(v -> mListener.onFragmentInteraction(CommunicationTag.OPEN_CHAT_FRAGMENT, channel));
    }

    /**
     * Refresh the posts by reading in the database
     */
    private void updatePosts() {
        FirebaseProxy.getInstance().onPostAddedInChannel(channel.getId(), user.getSciper(), result -> {
            posts.add(result);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Set up an onClick listener on the button to write a new post
     */
    private void setUpNewPostButton() {
        writePostButton.setOnClickListener(v -> mListener.onFragmentInteraction(CommunicationTag.OPEN_WRITE_POST_FRAGMENT, channel));
    }

    /**
     * This function is called when the user swipes down to refresh the list of posts
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        updatePosts();
    }
}
