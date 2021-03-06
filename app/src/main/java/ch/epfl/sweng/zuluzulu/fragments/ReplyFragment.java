package ch.epfl.sweng.zuluzulu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperChatPostsFragment;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.user.User;

import static ch.epfl.sweng.zuluzulu.fragments.superFragments.FragmentWithUser.ARG_USER;

public class ReplyFragment extends SuperChatPostsFragment {
    private static final int REPLY_MAX_LENGTH = 100;
    private static final String ARG_POST = "ARG_POST";

    private Post postOriginal;
    private PostArrayAdapter adapter;

    private EditText replyText;
    private ImageButton sendButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ReplyFragment() {
        // Required empty public constructor
    }

    public static ReplyFragment newInstance(User user, Channel channel, Post postOriginal) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        if(channel == null)
            throw new IllegalArgumentException("channel can't be null");
        if(postOriginal == null)
            throw new IllegalArgumentException("original post can't be null");
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_DATA, channel);
        args.putSerializable(ARG_POST, postOriginal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postOriginal = (Post) getArguments().getSerializable(ARG_POST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);

        ConstraintLayout rootView = view.findViewById(R.id.reply_fragment);
        listView = view.findViewById(R.id.reply_list_view);
        replyText = view.findViewById(R.id.reply_text_edit);
        sendButton = view.findViewById(R.id.reply_send_button);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_replies);

        rootView.setBackgroundColor(Color.parseColor(postOriginal.getColor()));

        messages.add(postOriginal);
        adapter = new PostArrayAdapter(view.getContext(), messages, user);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        sendButton.setEnabled(false);

        setUpReplyText();
        setUpSendButton();
        setUpProfileListener();
        loadReplies(false);

        return view;
    }

    /**
     * Set up the listener on the text edit field
     */
    private void setUpReplyText() {
        replyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                sendButton.setEnabled(0 < length && length < REPLY_MAX_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Set up the listener on the send button
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(v -> {
            String newId = DatabaseFactory.getDependency().getNewReplyId(postOriginal.getChannelId(), postOriginal.getId());
            Post post = new Post(
                    newId,
                    postOriginal.getChannelId(),
                    postOriginal.getId(),
                    replyText.getText().toString().trim().replaceAll("([\\n\\r]+\\s*)*$", ""),
                    anonymous ? "" : user.getFirstNames(),
                    user.getSciper(),
                    Timestamp.now().toDate(),
                    postOriginal.getColor(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            postOriginal.addReply(newId);
            DatabaseFactory.getDependency().addReply(post);
            loadReplies(true);
            replyText.getText().clear();
        });
    }

    /**
     * Refresh the replies by reading in the database
     */
    private void loadReplies(boolean newReply) {
        messages.clear();
        messages.add(postOriginal);
        DatabaseFactory.getDependency().getRepliesFromPost(postOriginal.getChannelId(), postOriginal.getId(), result -> {
            Log.d("REPLIES", result.size() + " replies");
            messages.addAll(result);
            Collections.sort(messages, (o1, o2) -> {
                if (o1.getTime().before(o2.getTime()))
                    return -1;
                else if (o1.getTime().after(o2.getTime()))
                    return 1;
                else
                    return 0;
            });
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            if (newReply) {
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    /**
     * This function is called when the user swipes down to refresh the list of replies
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadReplies(false);
    }
}
