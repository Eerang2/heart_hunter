package heart_link.infrastructure.util;

import heart_link.infrastructure.util.data.JwtProperties;
import heart_link.presentation.member.data.AuthConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 쿠키 관련 util 클래스
 *
 * @author 이진우
 * @since 2025.4.22
 * <p>
 * - xss 공격 방지와 https 전송 등 설정하고 쿠키에 저장하는 로직
 * - 쿠키에서 리프레시토큰 추출
 * - 저장할떄와 같은 설정으로 삭제 로직
 */
@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JwtProperties jwtProperties;

    //  쿠키에 리프레시 토큰 저장
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstants.COOKIE_REFRESH_TOKEN, refreshToken);
        cookie.setHttpOnly(true); // XSS 공격 방지
        cookie.setSecure(true);   // HTTPS에서만 전송
        cookie.setPath("/");      // 모든 경로에서 사용 가능
        cookie.setMaxAge((int) (jwtProperties.getRefreshTokenExpireTime() / 1000)); // 만료 시간 설정

        response.addCookie(cookie);
    }

    // refresh token 추출
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> AuthConstants.COOKIE_REFRESH_TOKEN.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    // 쿠키 삭제
    public void deleteRefreshToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstants.COOKIE_REFRESH_TOKEN, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);       // 즉시 만료
        response.addCookie(cookie);
    }
}
