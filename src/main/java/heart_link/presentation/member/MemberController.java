package heart_link.presentation.member;

import heart_link.application.member.repository.entity.MemberEntity;
import heart_link.application.member.service.AuthService;
import heart_link.infrastructure.provider.JwtProvider;
import heart_link.infrastructure.util.CookieUtil;
import heart_link.infrastructure.util.data.TokensResponseDTO;
import heart_link.presentation.member.data.AuthConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @GetMapping("/login/{memberId}")
    public String login(@PathVariable Long memberId,
                        HttpServletResponse response,
                        HttpSession session) {
        MemberEntity memberEntity = authService.findById(memberId).orElseThrow(IllegalArgumentException::new);
        TokensResponseDTO tokensResponseDTO = jwtProvider.generateToken(memberEntity);
        saveTokens(response, tokensResponseDTO);

        // 서버 세션에 저장
        session.setAttribute("accessToken", tokensResponseDTO.getAccessToken());
        return "redirect:/main";
    }

    private void saveTokens(HttpServletResponse response, TokensResponseDTO tokenRes) {
        // http 쿠키 저장
        cookieUtil.addRefreshTokenToCookie(tokenRes.getRefreshToken(), response);
        response.setHeader(AuthConstants.HEADER_AUTHORIZATION, AuthConstants.TOKEN_PREFIX + tokenRes.getAccessToken());
    }
}
