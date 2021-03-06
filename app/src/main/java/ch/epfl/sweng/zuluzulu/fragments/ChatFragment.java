package ch.epfl.sweng.zuluzulu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.Timestamp;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.adapters.ChatMessageArrayAdapter;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperChatPostsFragment;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.structure.SuperMessage;
import ch.epfl.sweng.zuluzulu.structure.user.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_POST_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.structure.SuperMessage.increasingTimeComparator;

/**
 * A {@link SuperChatPostsFragment} subclass.
 * This fragment is used to display the chat and to write in it
 */
public class ChatFragment extends SuperChatPostsFragment {
    private static final int MAX_MESSAGE_LENGTH = 100;
    private ImageButton sendButton;
    private EditText textEdit;

    private ChatMessageArrayAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(User user, Channel channel) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        if(channel == null)
            throw new IllegalArgumentException("channel can't be null");
        return (ChatFragment)newInstanceOf("chat", user, channel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        sendButton = view.findViewById(R.id.chat_send_button);
        textEdit = view.findViewById(R.id.chat_message_edit);
        listView = view.findViewById(R.id.chat_list_view);
        chatButton = view.findViewById(R.id.chat_button);
        postsButton = view.findViewById(R.id.posts_button);

        chatButton.setEnabled(false);
        chatButton.setBackgroundColor(getResources().getColor(R.color.white));
        postsButton.setEnabled(true);
        postsButton.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));

        sendButton.setEnabled(false);

        adapter = new ChatMessageArrayAdapter(view.getContext(), messages, user);
        listView.setAdapter(adapter);

        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        loadInitialMessages();
        setUpDataOnChangeListener();
        setUpSendButton();
        setUpEditText();
        setUpPostsButton();
        setUpProfileListener();

        return view;
    }

    /**
     * Add an onClick listener on the button to send the message to the database
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(v -> {
            ChatMessage chatMessage = new ChatMessage(
                    DatabaseFactory.getDependency().getNewMessageId(data.getId()),
                    data.getId(),
                    textEdit.getText().toString().trim().replaceAll("([\\n\\r]+\\s*)*$", ""),
                    Timestamp.now().toDate(),
                    anonymous ? "" : user.getFirstNames(),
                    user.getSciper());
            DatabaseFactory.getDependency().addMessage(chatMessage);
            textEdit.getText().clear();
        });
    }

    /**
     * Add an onClick listener on the button to switch to the posts fragment
     */
    private void setUpPostsButton() {
        postsButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_POST_FRAGMENT, data));
    }

    /**
     * Add a listener on the edit text view to check that the message is not empty
     */
    private void setUpEditText() {
        textEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                int length = text.toString().length();
                sendButton.setEnabled(0 < length && length < MAX_MESSAGE_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable text) {
            }
        });
    }

    /**
     * Refresh the chat by reading all the messages in the database
     */
    private void loadInitialMessages() {
        DatabaseFactory.getDependency().getMessagesFromChannel(data.getId(), result -> {
            messages.clear();
            messages.addAll(result);
            sortMessages();
        });
    }

    /**
     * Set up the listener on database changes to update the list of messages
     */
    private void setUpDataOnChangeListener() {
        DatabaseFactory.getDependency().updateOnNewMessagesFromChannel(data.getId(), result -> {
            messages.clear();
            messages.addAll(result);
            sortMessages();
        });
    }

    /**
     * Sort the list of messages by time and notify adapter
     */
    private void sortMessages() {
        Collections.sort(messages, (Comparator<SuperMessage>) increasingTimeComparator());
        adapter.notifyDataSetChanged();
        listView.setSelection(adapter.getCount() - 1);
    }
}
