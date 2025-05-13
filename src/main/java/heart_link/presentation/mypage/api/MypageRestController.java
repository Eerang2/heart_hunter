package heart_link.presentation.mypage.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageRestController {

    @GetMapping("/mypage")
    public String showMypage() {
        return "mypage/mypage";  // templates/mypage/mypage.html
    }
}
