package heart_link.presentation.member.api;

import heart_link.application.files.FileUpload;
import heart_link.application.member.enums.ProviderType;
import heart_link.application.member.repository.entity.MemberEntity;
import heart_link.application.member.repository.entity.ProfileImageEntity;
import heart_link.application.member.service.AuthService;
import heart_link.infrastructure.provider.JwtProvider;
import heart_link.infrastructure.util.CookieUtil;
import heart_link.infrastructure.util.data.TokensResponseDTO;
import heart_link.presentation.member.data.AuthConstants;
import heart_link.presentation.member.data.request.MemberSignUpReq;
import heart_link.presentation.member.data.request.OAuthStateReq;
import heart_link.presentation.member.data.response.MemberRes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final AuthService authService;
    private final FileUpload<ProfileImageEntity> fileUpload;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @PostMapping(value = "/api/member/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> signup(@RequestPart("data") @Valid MemberSignUpReq member,
                                 @RequestPart("images") List<MultipartFile> images,
                                 HttpSession session,
                                 HttpServletResponse response
    ) {
        ProviderType providerType = (ProviderType) session.getAttribute("providerType");
        List<ProfileImageEntity> imageUrls = fileUpload.upload(images);
        Long id = authService.signup(imageUrls, member, providerType).getId();

        return ResponseEntity.ok(id);
    }


    @PostMapping("/setOauthState")
    public String setOauthState(@RequestBody OAuthStateReq request, HttpSession session) {
        // 클라이언트에서 전달한 oauth_state 값을 세션에 저장
        session.setAttribute("oauth_state", request.getOauthState());
        session.setAttribute("oauth_state_created_at", System.currentTimeMillis());

        return "OAuth state has been set in session!";
    }

    @PostMapping("/api/agreements")
    public void agreement(@RequestBody Map<String, Boolean> agreements, HttpSession session) {
        session.setAttribute("agreements", agreements);
    }

    private void saveTokens(TokensResponseDTO member, HttpServletResponse response) {
        cookieUtil.addRefreshTokenToCookie(member.getRefreshToken(), response);
        response.setHeader(AuthConstants.HEADER_AUTHORIZATION, AuthConstants.TOKEN_PREFIX + member.getAccessToken());
    }

}
