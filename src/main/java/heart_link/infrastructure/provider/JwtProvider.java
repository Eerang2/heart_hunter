package heart_link.infrastructure.provider;

import heart_link.application.member.repository.RefreshTokenRepository;
import heart_link.application.member.repository.entity.MemberEntity;
import heart_link.application.member.repository.entity.RefreshTokenEntity;
import heart_link.infrastructure.util.JwtUtil;
import heart_link.infrastructure.util.data.JwtPayloadResponseDTO;
import heart_link.infrastructure.util.data.TokensResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;


    public TokensResponseDTO generateToken(MemberEntity memberEntity) {
        // 이전 리프레시 토큰 존재 시 삭제
        refreshTokenRepository.findByMemberId(memberEntity.getId())
                .ifPresent(refreshTokenRepository::delete);

        String accessToken = jwtUtil.generateAccessToken(JwtPayloadResponseDTO.of(memberEntity));
        String refreshToken = jwtUtil.generateRefreshToken(memberEntity.getId());

        RefreshTokenEntity newToken = RefreshTokenEntity.of(memberEntity.getId(), refreshToken);
        refreshTokenRepository.save(newToken);

        return new TokensResponseDTO(accessToken, refreshToken);
    }
}
