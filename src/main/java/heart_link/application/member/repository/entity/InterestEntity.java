package heart_link.application.member.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Interest")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_key")
    private Long id;

    private String tag;

    private Long memberId;

    public static InterestEntity of(String tag, Long memberId) {
        return new InterestEntity(tag, memberId);
    }

    public InterestEntity(String tag, Long memberId) {
        this.memberId = memberId;
        this.tag = tag;
    }
}
