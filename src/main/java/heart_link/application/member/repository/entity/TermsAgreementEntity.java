package heart_link.application.member.repository.entity;

import heart_link.application.member.enums.TermsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TERMS_AGREEMENT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsAgreementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_agree_key")
    private Long id;

    // 약관 항목
    private TermsType termsType;

    // 동의 여부
    private Boolean agree;

    // 필수 동의 여부
    private Boolean required;

    // 동의한 시간
    private LocalDateTime agreedAt;

    // 유저 pk
    private Long memberId;


}
