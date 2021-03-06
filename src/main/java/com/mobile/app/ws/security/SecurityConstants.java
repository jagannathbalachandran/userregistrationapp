package com.mobile.app.ws.security;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String LOGIN_URL = "/users/login";
    public static final String TOKEN_SECRET = "jf9i4jgu83nfl0";

    public static final String USER_ID_STRING = "user-id";
    public static final String EMAIL_VERIFICATION_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
}
