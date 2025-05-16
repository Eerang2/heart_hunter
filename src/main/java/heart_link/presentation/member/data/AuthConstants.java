package heart_link.presentation.member.data;

import org.springframework.http.HttpHeaders;

public class AuthConstants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = HttpHeaders.AUTHORIZATION;
    public static final String COOKIE_REFRESH_TOKEN = "refreshToken";
}
