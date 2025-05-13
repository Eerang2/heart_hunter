package heart_link.application.member.repository.entity;

import heart_link.application.member.enums.Gender;
import heart_link.presentation.member.data.request.MemberSignUpReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_key")
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Gender lookingFor;

    private String birth;

    public MemberEntity toEntity(MemberSignUpReq req) {
        return MemberEntity.builder()
                .name(req.getName())
                .email(req.getEmail())
                .gender(req.getGender())
                .lookingFor(req.getLookingFor())
                .birth(req.getBirthday())
                .build();
    }



}
