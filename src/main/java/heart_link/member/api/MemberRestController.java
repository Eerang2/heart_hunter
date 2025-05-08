package heart_link.member.api;

import heart_link.application.files.FileUpload;
import heart_link.application.member.repository.entity.ProfileImageEntity;
import heart_link.application.member.service.AuthService;
import heart_link.member.data.request.MemberSignUpReq;
import heart_link.member.data.request.OAuthStateReq;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MemberRestController {

    private final AuthService authService;
    private final FileUpload<ProfileImageEntity> fileUpload;

    @PostMapping(value = "/api/member/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> signup(@RequestPart("data") MemberSignUpReq data,
                                 @RequestPart("images") List<MultipartFile> images,
                                 HttpSession session
    ) {
        List<ProfileImageEntity> imageUrls = fileUpload.upload(images);
        Long memberId = authService.signup(imageUrls, data);

        // ✅ 세션 키 제거
        session.removeAttribute("memberDto");
        session.removeAttribute("oauth_state");
        return ResponseEntity.ok(memberId);
    }


    @PostMapping("/setOauthState")
    public String setOauthState(@RequestBody OAuthStateReq request, HttpSession session) {
        // 클라이언트에서 전달한 oauth_state 값을 세션에 저장
        session.setAttribute("oauth_state", request.getOauthState());
        session.setAttribute("oauth_state_created_at", System.currentTimeMillis());

        return "OAuth state has been set in session!";
    }

}
