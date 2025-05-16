package heart_link.application.member.service;

import heart_link.application.exceptions.enums.ErrorCode;
import heart_link.application.exceptions.auth.InvalidInterestException;
import heart_link.application.exceptions.auth.InvalidProfileImageException;
import heart_link.application.member.enums.ImageRole;
import heart_link.application.member.enums.ProviderType;
import heart_link.application.member.enums.TermsType;
import heart_link.application.member.repository.InterestRepository;
import heart_link.application.member.repository.MemberRepository;
import heart_link.application.member.repository.ProfileImageRepository;
import heart_link.application.member.repository.TermsAgreementRepository;
import heart_link.application.member.repository.entity.InterestEntity;
import heart_link.application.member.repository.entity.MemberEntity;
import heart_link.application.member.repository.entity.ProfileImageEntity;
import heart_link.application.member.repository.entity.TermsAgreementEntity;
import heart_link.presentation.member.data.request.MemberSignUpReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;
    private final InterestRepository interestRepository;
    private final TermsAgreementRepository termsAgreementRepository;

    @Transactional
    public MemberEntity signup(List<ProfileImageEntity> imageUrls, MemberSignUpReq request, ProviderType providerType) {

        MemberEntity member = createAndSaveMember(request, providerType);
        Long memberId = memberRepository.save(member).getId();

        List<ProfileImageEntity> images = processProfileImages(imageUrls, memberId);
        List<InterestEntity> interests = processInterests(request.getInterests(), memberId);
        List<TermsAgreementEntity> agreements = processTermsAgreements(request.getAgreements(), memberId);


        profileImageRepository.saveAll(images);
        interestRepository.saveAll(interests);
        termsAgreementRepository.saveAll(agreements);

        return member;
    }

    // 회원 저장
    private MemberEntity createAndSaveMember(MemberSignUpReq request, ProviderType providerType) {
        MemberEntity entity = new MemberEntity().toEntity(request, providerType);
        return memberRepository.save(entity);
    }


    // 프로필 이미지 검사후 List<ProfileImageEntity>에 데이터 저장
    private List<ProfileImageEntity> processProfileImages(List<ProfileImageEntity> rawImages, Long memberId) {
        if (rawImages == null || rawImages.isEmpty()) {
            throw new InvalidProfileImageException(ErrorCode.MISSING_REQUIRED_FIELDS);
        } else if (rawImages.size() > 3) {
            throw new InvalidProfileImageException(ErrorCode.VALID_IMAGE_COUNT);
        }

        return IntStream.range(0, rawImages.size())
                .mapToObj(i -> {
                    ProfileImageEntity raw = rawImages.get(i);
                    ImageRole role = (i == 0) ? ImageRole.MAIN : ImageRole.SUB;
                    return ProfileImageEntity.of(raw, memberId, role);
                })
                .toList();
    }

    // 괸심사 검사후 List<InterestEntity>에 데이터 저장
    private List<InterestEntity> processInterests(List<String> interests, Long memberId) {
        if (interests == null || interests.isEmpty()) {
            throw new InvalidInterestException(ErrorCode.MISSING_REQUIRED_FIELDS);
        } else if (interests.size() > 6) {
            throw new InvalidInterestException(ErrorCode.INVALID_REQUEST_BODY);
        }
        return interests.stream()
                .map(interest -> InterestEntity.of(interest, memberId))
                .toList();
    }

    public Optional<MemberEntity> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<MemberEntity> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }


    private List<TermsAgreementEntity> processTermsAgreements(Map<TermsType, Boolean> agreements, Long memberId) {
        List<TermsAgreementEntity> list = new ArrayList<>();
        for (Map.Entry<TermsType, Boolean> entry : agreements.entrySet()) {
            TermsType type = entry.getKey();
            Boolean agreed = entry.getValue();

            TermsAgreementEntity agreement = TermsAgreementEntity.builder()
                    .termsType(type)
                    .agree(agreed)
                    .required(isRequired(type))
                    .agreedAt(agreed ? LocalDateTime.now() : null)
                    .memberId(memberId)
                    .build();
            list.add(agreement);
        }
        return list;
    }

    private boolean isRequired(TermsType type) {
        return type != TermsType.MARKETING;
    }
}
