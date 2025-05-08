package heart_link.application.member.repository.entity;


import heart_link.application.member.enums.ImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROFILE_IMAGE")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_key")
    private Long id;

    private String url;

    @Enumerated(EnumType.STRING)
    private ImageType type;

    private Long memberId;

    public static ProfileImageEntity of(String url, ImageType type) {
        return new ProfileImageEntity(url, type);
    }

    public static ProfileImageEntity of(ProfileImageEntity profileImageEntity, Long memberId) {
        return ProfileImageEntity.builder()
                .url(profileImageEntity.getUrl())
                .type(profileImageEntity.getType())
                .memberId(memberId)
                .build();
    }

    public ProfileImageEntity(String url, ImageType type) {
        this.url = url;
        this.type = type;
    }
}
