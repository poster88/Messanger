package com.example.user.simplechat.utils;

/**
 * Created by User on 031 31.10.17.
 */

public final class Const {
    public static final String REF_USERS = "https://messager-c419d.firebaseio.com/UserInfo/";
    public static final String CHAT_ARCHIVE = "ChatArchive";
    public static final String CHAT_ID_TABLE = "ChatIDTable";

    public static final String USER_ID = "userID";
    public static final String RECEIVER_ID = "receiverID";
    public static final String CHAT_ID = "chatID";
    public static final String USERS_IMAGES = "users_images";
    public static final String USER_INFO = "UserInfo";
    public static final String PHOTO_DEFAULT = "default";
    public static final String PHOTO_URI = "photo_uri";

    /*** fragments tags ***/
    public static final String REGISTRATION_TAG = "RegistrationFragment";
    public static final String CHAT_LIST_TAG = "ChatListFragment";
    public static final String CHAT_FRAG_TAG = "ChatFragment";
    public static final String LOGIN_FRAG_TAG = "LoginFragment";
    public static final String TAG = "LOG_TAG";

    /*** keys for onSaveInstance ***/
    public static final String IS_TASK_RUNNING_KEY = "isTaskRunning";
    public static final String USER_IMAGE_KEY = "userImageKey";
    public static final String USER_LIST_DATA_KEY = "userListDataKey";
    public static final String CHAT_LIST_DATA_KEY = "chatListDataKey";
    public static final String CHAT_ID_TABLE_DATA_KEY = "enabledChatUsersDataKey";
    public static final String LAYOUT_MANAGER_KEY = "layoutManagerKey";
    public static final String DEFAULT_IMAGE_KEY = "default";
    public static final String QUERY_NAME_KEY = "userName";

    /*** keys for onActivityResult ***/
    public static final int REQUEST_READ_PERMISSION = 9003;
    public static final int PHOTO_REQUEST = 9002;
    public static final int RESULT_OK = -1;
}
