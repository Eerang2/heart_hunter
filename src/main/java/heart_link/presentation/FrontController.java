package heart_link.presentation;

import heart_link.application.member.service.NaverAuthService;
import heart_link.member.data.response.MemberRes;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final NaverAuthService naverAuthService;

    @GetMapping("/")
    public String index() {
        return "member/start";
    }

    @GetMapping("/signup")
    public String signup(HttpSession session, Model model) {

        MemberRes member = (MemberRes) session.getAttribute("member");
        model.addAttribute("member",member);

        return "member/createMember";
    }

    /**
     * 네이버 본인 인증 API
     * @return 회원가입 리다이렉트
     */
    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpSession session
    ) throws Exception {

        MemberRes member = naverAuthService.authenticate(code, state, session);
        session.setAttribute("member", member);
        session.setMaxInactiveInterval(30 * 60);
        return "redirect:/signup";
    }

}
