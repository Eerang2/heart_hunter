package heart_link.presentation.member.data.request;

import heart_link.application.member.enums.Gender;
import heart_link.application.member.enums.TermsType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberSignUpReq {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Email
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    private String password;

    @NotNull
    private Gender gender;

    @NotNull
    private Gender lookingFor;

    @NotNull
    private String birthday;

    @NotEmpty
    private List<String> interests;

    private Map<TermsType, Boolean> agreements;
}
