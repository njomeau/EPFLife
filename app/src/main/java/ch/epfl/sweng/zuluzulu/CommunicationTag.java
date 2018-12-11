package ch.epfl.sweng.zuluzulu;

public enum CommunicationTag {

    // Utils to modify the main activity
    SET_TITLE,
    SET_USER,

    // Opening fragments
    OPEN_MAIN_FRAGMENT,
    OPEN_SETTINGS_FRAGMENT,
    OPEN_ABOUT_US_FRAGMENT,
    OPEN_PROFILE_FRAGMENT,
    OPEN_LOGIN_FRAGMENT,
    OPEN_CHAT_FRAGMENT,
    OPEN_POST_FRAGMENT,
    OPEN_WRITE_POST_FRAGMENT,
    OPEN_REPLY_FRAGMENT,
    OPEN_CHANNEL_FRAGMENT,
    OPEN_EVENT_FRAGMENT,
    OPEN_EVENT_DETAIL_FRAGMENT,
    OPEN_ASSOCIATION_FRAGMENT,
    OPEN_ASSOCIATION_DETAIL_FRAGMENT,

    // Admin tags
    OPEN_CREATE_EVENT,
    OPEN_MEMENTO,
    OPEN_MANAGE_CHANNEL,
    OPEN_MANAGE_USER,
    OPEN_MANAGE_ASSOCIATION
}
