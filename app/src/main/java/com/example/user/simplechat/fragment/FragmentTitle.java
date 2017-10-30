package com.example.user.simplechat.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by POSTER on 28.10.2017.
 */

public class FragmentTitle extends Fragment {
    public static final String REF_USERS = "https://messager-c419d.firebaseio.com/UserInfo/";

    public static final String LOGIN_FRAG = "LoginFragment";
    public static final String REGISTRATION_FRAG = "RegistrationFragment";
    public static final String CHAT_LIST_FRAG = "ChatListFragment";
    public static final String USER_ID = "userID";
    public static final String USERS_IMAGES = "users_images";
    public static final String USER_INFO = "UserInfo";
    public static final String PHOTO_DEFAULT = "default";
    public static final String TAG = "LOG_TAG";
    public static final String CHAT_USER_INFO = "CHAT_USER_INFO";
    public static final String PHOTO_URI = "photo_uri";

    public static int REQUEST_READ_PERMISSION = 9003;
    public static int PHOTO_REQUEST = 9002;
    public static int RESULT_OK = -1;
}
