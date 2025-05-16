package heart_link.application.member.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "REFRESH_TOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;


    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    public static RefreshTokenEntity of(Long userId, String refreshToken) {
        return new RefreshTokenEntity(userId, refreshToken, LocalDateTime.now(), null);
    }

    public RefreshTokenEntity(Long memberId, String refreshToken, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }
}
