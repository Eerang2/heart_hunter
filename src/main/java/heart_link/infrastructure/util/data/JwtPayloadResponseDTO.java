package heart_link.infrastructure.util.data;

import heart_link.application.member.enums.Gender;
import heart_link.application.member.repository.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPayloadResponseDTO {

    private Long memberId;

    private Gender gender;

    public static JwtPayloadResponseDTO of(MemberEntity member) {
        return new JwtPayloadResponseDTO(member.getId(), member.getGender());
    }
}
