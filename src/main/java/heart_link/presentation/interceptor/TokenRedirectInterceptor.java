package heart_link.presentation.interceptor;

import heart_link.application.member.enums.Gender;
import heart_link.application.member.repository.MemberRepository;
import heart_link.application.member.repository.entity.MemberEntity;
import heart_link.infrastructure.util.CookieUtil;
import heart_link.infrastructure.util.JwtUtil;
import heart_link.infrastructure.util.data.JwtPayloadResponseDTO;
import heart_link.presentation.member.data.AuthConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenRedirectInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private static final String START_FORM_URL = "/";
    private static final String MAIN_FORM_URL = "/main";


    public TokenRedirectInterceptor(JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // "/" 경로일 때만 처리
        if (!request.getRequestURI().equals(START_FORM_URL)) {
            return true;
        }

        String accessToken = jwtUtil.extractAccessTokenFromRequest(request);
        String refreshToken = cookieUtil.extractRefreshTokenFromCookie(request);

        // [1] 액세스 토큰 만료 + 리프레시 유효 → 재발급 후 메인으로 리다이렉트
        if (accessToken != null && refreshToken != null
                && !jwtUtil.validateAccessToken(accessToken) // 액세스 토큰 만료
                && jwtUtil.validateRefreshToken(refreshToken)) { // 리프레시 유효

            Long memberId = jwtUtil.extractMemberId(refreshToken);
            // 임시로 gender는 accessToken에서 꺼냄 (물론 accessToken이 만료되어도 payload는 읽힘)
            String genderStr = jwtUtil.extractGenderFromExpiredToken(accessToken);

            JwtPayloadResponseDTO payloadDTO = JwtPayloadResponseDTO.builder()
                    .memberId(memberId)
                    .gender(Gender.valueOf(genderStr))
                    .build();

            String newAccessToken = jwtUtil.generateAccessToken(payloadDTO);
            response.setHeader(AuthConstants.HEADER_AUTHORIZATION, AuthConstants.TOKEN_PREFIX + newAccessToken);
            response.sendRedirect(MAIN_FORM_URL);
            return false;
        }

        // [2] 액세스 토큰만 있고 유효하지 않음 → 그냥 리다이렉트
        if (accessToken != null && !jwtUtil.validateAccessToken(accessToken)) {
            response.sendRedirect(MAIN_FORM_URL);
            return false;
        }

        return true; // 토큰 없음 → 기본 "/"
    }


}
