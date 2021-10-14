package br.com.dolphinCards.config;

public class SecurityConstants {
    public static final String SECRET = "MEINE_SECRET_KEY";
    public static final long EXPIRATION_TIME = 1200_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/auth/signup";
}