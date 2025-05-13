package heart_link.presentation.member.data.response;

import heart_link.application.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberRes {

    private String email;
    private Gender gender;
    private String name;

    public static MemberRes of(String email, Gender gender, String name) {
        return new MemberRes(email, gender, name);
    }
}
