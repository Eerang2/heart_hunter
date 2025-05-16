package heart_link.infrastructure.util;


import heart_link.application.member.enums.Gender;
import heart_link.infrastructure.util.data.JwtPayloadResponseDTO;
import heart_link.infrastructure.util.data.JwtProperties;
import heart_link.presentation.member.data.AuthConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * jwt 관련 util 클래스
 *
 * @author 이진우
 * @since 2025.4.23
 * <p>
 * - jwt시크릿 키와 만료기간 properties 생성자 생성
 */
@Component
public class JwtUtil {

    private final String secretKeyString;
    private final JwtProperties jwtProperties;
    private static final Logger log = LogManager.getLogger(JwtUtil.class);

    public JwtUtil(@Value("${jwt.secret}") String secretKeyString, JwtProperties jwtProperties) {
        this.secretKeyString = secretKeyString;
        this.jwtProperties = jwtProperties;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }


    /**
     * memberId 와 memberName 이 저장된 엑세스 토큰 생성
     *
     * @param payload memberId/memberName
     * @return access token
     */
    public String generateAccessToken(JwtPayloadResponseDTO payload) {
        if (payload.getMemberId() == null || payload.getGender() == null) {
            throw new IllegalArgumentException();
        }

        return Jwts.builder()
                .setSubject(payload.getMemberId().toString())
                .claim("gender", payload.getGender().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpireTime()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * memberId만 저장된 리프레시 토큰 생성
     *
     * @param memberId 유저 pk
     * @return refresh token
     */
    public String generateRefreshToken(Long memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpireTime()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 리프레시 토큰이 유효한지 검증하는 메서드
     * - 서명 검증 및 파싱이 정상적으로 이루어지면 true 반환
     * - 예외 발생 시 유효하지 않은 토큰으로 판단하고 false 반환
     *
     * @param refreshToken 리프레사 토큰
     * @return ture/false
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            extractAllClaims(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 액세스 토큰의 유효성을 검증
     * - 토큰 파싱 과정에서 발생하는 각 예외에 따라 로그 처리,
     *
     * @param token 엑세스 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false 를 반환
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (IllegalArgumentException e) {
            log.warn("Illegal argument token: {}", token, e);
        }
        return false;
    }

    /**
     * 리프레시 토큰에서 memberId(subject)를 추출하
     * - 토큰을 파싱하여 subject 값을 꺼내고 Long 타입으로 변환하여 반환
     *
     * @param refreshToken 리프레시 토큰
     * @return member PK
     */
    public Long extractMemberId(String refreshToken) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject());
    }

    /**
     * 엑세스 토큰에서 만료일 추출
     *
     * @param accessToken 엑세스 토큰
     * @return 엑세스토큰 만료일
     */
    public LocalDateTime extractExpiration(String accessToken) {
        Claims claims = extractAllClaims(accessToken);
        Date expiration = claims.getExpiration(); // Date 타입 반환됨
        return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
    }

    public String extractAccessTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.HEADER_AUTHORIZATION);
        if (header != null && header.startsWith(AuthConstants.TOKEN_PREFIX)) {
            return header.substring(7);
        }
        return null;
    }


    // 토큰 읽고 클레임 꺼내오는 메서드
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public JwtPayloadResponseDTO extractPayloadDTO(String accessToken) {
        Claims claims = extractAllClaims(accessToken);

        Long memberId = Long.valueOf(claims.getSubject()); // 수정됨
        String genderStr = claims.get("gender", String.class);

        Gender gender = Gender.valueOf(genderStr);

        return JwtPayloadResponseDTO.builder()
                .memberId(memberId)
                .gender(gender)
                .build();
    }

    public String extractGenderFromExpiredToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("gender", String.class);
        } catch (JwtException e) {
            log.error("Failed to extract gender from token", e);
            return null;
        }
    }

}
