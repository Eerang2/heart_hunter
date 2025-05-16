package heart_link.presentation.member.auth;

import heart_link.application.member.enums.ProviderType;
import heart_link.application.member.repository.entity.MemberEntity;
import heart_link.application.member.service.AuthService;
import heart_link.application.member.service.NaverAuthService;
import heart_link.infrastructure.provider.JwtProvider;
import heart_link.infrastructure.util.CookieUtil;
import heart_link.infrastructure.util.data.TokensResponseDTO;
import heart_link.presentation.member.data.AuthConstants;
import heart_link.presentation.member.data.response.MemberRes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SocialLoginController {

    private final NaverAuthService naverAuthService;
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    /**
     * 네이버 본인 인증 API
     * @return 회원가입 리다이렉트
     */
    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpSession session,
                                HttpServletResponse response
    ) throws Exception {

        MemberRes member = naverAuthService.authenticate(code, state, session);
        Optional<MemberEntity> memberEntity = authService.findByEmail(member.getEmail());

        if (memberEntity.isPresent()) {
            Long id = memberEntity.orElseThrow(IllegalStateException::new).getId();
            return "redirect:/login/" + id;
        } else {
            session.setAttribute("member", member);
            session.setAttribute("providerType", ProviderType.NAVER);
            session.setMaxInactiveInterval(30 * 60);
            return "redirect:/member/dongui";
        }
    }

    private void saveTokens(TokensResponseDTO member, HttpServletResponse response) {
        cookieUtil.addRefreshTokenToCookie(member.getRefreshToken(), response);
        response.setHeader(AuthConstants.HEADER_AUTHORIZATION, AuthConstants.TOKEN_PREFIX + member.getAccessToken());
    }
}
