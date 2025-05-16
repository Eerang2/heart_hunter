package heart_link.application.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import heart_link.application.exceptions.social.CSRFAttackDetectedException;
import heart_link.application.exceptions.enums.ErrorCode;
import heart_link.application.exceptions.auth.TimeoutRequestException;
import heart_link.application.member.enums.Gender;
import heart_link.presentation.member.data.response.MemberRes;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class NaverAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${naver.client.id}") // application.yml 또는 properties에 설정된 NAVER Client ID 주입
    private String CLIENT_ID;

    @Value("${naver.client.secret}") // NAVER Client Secret 주입
    private String CLIENT_SECRET;

    private static final String TOKEN_PREFIX = "Bearer "; // Authorization 헤더에 붙는 접두사
    private static final int TIME_LIMIT = 10 * 60 * 1000; // OAuth state 유효시간 (10분)

    // 네이버 인증 절차 메서드
    public MemberRes authenticate(String code, String state, HttpSession session) throws Exception {

        // 세션에 저장된 state (CSRF 방어용) 와 생성 시간 꺼내기
        String savedState = (String) session.getAttribute("oauth_state");
        Long createdAt = (Long) session.getAttribute("oauth_state_created_at");

        // 1. CSRF 공격 방지: state 값이 불일치하면 예외 발생
        if (!state.equals(savedState)) throw new CSRFAttackDetectedException(ErrorCode.AUTH_CSRF_ATTACK_DETECTED);

        // 2. 시간 초과 검증: 생성된 지 10분이 넘으면 예외 발생
        if (System.currentTimeMillis() - createdAt > TIME_LIMIT)
            throw new TimeoutRequestException(ErrorCode.TIMEOUT_REQUEST);

        // 3. 액세스 토큰 요청 URL 생성
        String tokenUrl = UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("code", code)
                .queryParam("state", state)
                .queryParam("redirect_uri", "http://localhost:8080/naver/callback") // 네이버에 등록된 redirect_uri와 동일해야 함
                .build()
                .encode()
                .toUriString();

        // 4. 액세스 토큰 요청 (GET 방식)
        ResponseEntity<String> tokenResponse = restTemplate.getForEntity(tokenUrl, String.class);

        // 5. 응답에서 access_token 파싱
        String accessToken = objectMapper.readTree(tokenResponse.getBody())
                .get("access_token").asText();

        // 6. 사용자 정보 요청을 위한 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken);

        // 7. 사용자 정보 요청 (Authorization 헤더 포함)
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> profileResponse = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, String.class);

        // 8. 응답에서 사용자 정보 추출
        JsonNode profile = objectMapper.readTree(profileResponse.getBody()).get("response");

        // 9. 사용자 정보 객체(MemberRes)로 변환하여 반환
        return MemberRes.
                of(
                profile.get("email").asText(),
                Gender.valueOf(profile.get("gender").asText()), // 성별 문자열이 Enum과 정확히 매칭되어야 함
                profile.get("name").asText()
        );
    }
}
