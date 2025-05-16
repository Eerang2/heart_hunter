package heart_link.application.member.repository;

import heart_link.application.member.repository.entity.TermsAgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsAgreementRepository extends JpaRepository<TermsAgreementEntity, Long> {
}
