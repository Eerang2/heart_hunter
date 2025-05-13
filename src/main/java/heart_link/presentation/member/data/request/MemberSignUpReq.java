package heart_link.presentation.member.data.request;

import heart_link.application.member.enums.Gender;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberSignUpReq {

    private String name;
    private String email;
    private Gender gender;
    private String birthday;
    private Gender lookingFor;
    private List<String> interests;
}
