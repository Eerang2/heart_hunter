package heart_link.presentation;

import heart_link.presentation.member.data.response.MemberRes;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class FrontController {

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

    @GetMapping("/member/dongui")
    public String termAgree() {
        return "member/dongui";
    }


    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        return "index";
    }


}
