package ch.epfl.sweng.zuluzulu.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.Structure.SuperMessage;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

/**
 * A simple {@link SuperFragment} subclass.
 */
public abstract class SuperChatPostsFragment extends SuperFragment {

    protected static final String ARG_USER = "ARG_USER";
    protected static final String ARG_CHANNEL = "ARG_CHANNEL";
    protected static final String ARG_POST = "ARG_POST";

    protected List<SuperMessage> messages = new ArrayList<>();

    protected ListView listView;
    protected Button chatButton;
    protected Button postsButton;

    protected AuthenticatedUser user;
    protected Channel channel;

    protected boolean anonymous;

    public static SuperChatPostsFragment newInstanceOf(String type, User user, Channel channel) {
        SuperChatPostsFragment fragment;
        switch (type) {
            case "chat":
                fragment = new ChatFragment();
                break;
            case "post":
                fragment = new PostFragment();
                break;
            default:
                throw new IllegalArgumentException("The type is incorrect");
        }
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            channel = (Channel) getArguments().getSerializable(ARG_CHANNEL);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, channel.getName());
        }
    }

    /**
     * Set up long click listener on the posts or messages
     */
    protected void setUpProfileListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SuperMessage message = messages.get(position);
                if (!message.isAnonymous() && !message.isOwnMessage(user.getSciper())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    AlertDialog dlg = builder.setTitle("Visiter le profil de " + message.getSenderName() + " ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseFactory.getDependency().getUserWithIdOrCreateIt(message.getSenderSciper(), result -> {
                                        mListener.onFragmentInteraction(CommunicationTag.OPEN_PROFILE_FRAGMENT, result);
                                    });
                                }
                            })
                            .create();
                    dlg.setCanceledOnTouchOutside(true);
                    dlg.show();
                }
                return true;
            }
        });
    }
}
