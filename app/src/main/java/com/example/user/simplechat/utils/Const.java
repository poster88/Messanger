package com.example.user.simplechat.utils;

/**
 * Created by User on 031 31.10.17.
 */

public final class Const {
    public static final String REF_USERS = "https://messager-c419d.firebaseio.com/UserInfo/";
    public static final String CHAT_ARCHIVE = "ChatArchive";
    public static final String CHAT_ID_TABLE = "ChatIDTable";
    public static final String CHAT_USER_INFO = "UserInfo";

    public static final String RECEIVER_ID = "receiverID";
    public static final String CHAT_ID = "chatID";
    public static final String USERS_IMAGES = "users_images";
    public static final String USER_INFO = "UserInfo";
    public static final String PHOTO_DEFAULT = "default";

    //fragments tags
    public static final String REGISTRATION_TAG = "RegistrationFragment";
    public static final String CHAT_LIST_TAG = "ChatListFragment";
    public static final String CHAT_FRAG_TAG = "ChatFragment";

    //keys for onSaveInstance
    public static final String USER_LIST_DATA_KEY = "userListDataKey";
    public static final String CHAT_LIST_DATA_KEY = "chatListDataKey";
    public static final String CHAT_ID_TABLE_DATA_KEY = "enabledChatUsersDataKey";
    public static final String LAYOUT_MANAGER_KEY = "layoutManagerKey";
    public static final String DEFAULT_IMAGE_KEY = "default";
    public static final String QUERY_NAME_KEY = "userName";
    public static final String REC_PHOTO_B_KEY = "receiverPhotoArray";
    public static final String MY_PHOTO_B_KEY = "myPhotoArray";

    //keys for onActivityResult
    public static final int REQUEST_READ_PERMISSION = 9003;
    public static final int PHOTO_REQUEST = 9002;
    public static final int RESULT_OK = -1;
    public static final int VIEW_TYPE_LEFT = 0;
    public static final int VIEW_TYPE_RIGHT = 1;

    //keys for BroadcastReceiver actions
    public static final String USER_ONLINE = "USER_ONLINE";
    public static final String USER_OFFLINE = "USER_OFFLINE";
    public static final String USER_LOG_OFF = "USER_LOG_OFF";

    //keys for Service
    public static final String UPDATE_ONLINE_STATUS = "UPDATE_ONLINE_STATUS";
    public static final String CURRENT_ID_KEY = "CURRENT_ID_KEY";
    public static final String ONLINE_STATUS_KEY = "ONLINE_STATUS_KEY";
    public static final String UPDATE_PROFILE = "UPDATE_PROFILE";

    //key for Log
    public static final String MY_LOG = "MY_LOG";

    //key for Bundle
    public static final String EMAIL_KEY = "EMAIL_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
    public static final String NAME_KEY = "NAME_KEY";
    public static final String URI_PHOTO_KEY = "URI_PHOTO_KEY";
    public static final String IMAGE_ARRAY_KEY = "IMAGE_ARRAY_KEY";

    //key for Handler
    public static final int TASK_FAIL = 0;
    public static final int SIGN_IN_OK = 1;
    public static final int REG_OK = 2;

    //key for sharedPreferences
    public static final String USER_DATA = "USER_DATA";
    public static final String USER_IMAGE_KEY = "USER_IMAGE_KEY";

}
