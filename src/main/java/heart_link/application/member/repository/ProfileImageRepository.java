package heart_link.application.member.repository;

import heart_link.application.member.repository.entity.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long> {
}
