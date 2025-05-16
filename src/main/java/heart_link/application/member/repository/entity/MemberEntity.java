package heart_link.application.member.repository.entity;

import heart_link.application.member.enums.Gender;
import heart_link.application.member.enums.ProviderType;
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

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Gender lookingFor;

    private String birth;

    public MemberEntity toEntity(MemberSignUpReq req, ProviderType providerType) {
        return MemberEntity.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .gender(req.getGender())
                .providerType(providerType)
                .lookingFor(req.getLookingFor())
                .birth(req.getBirthday())
                .build();
    }



}
