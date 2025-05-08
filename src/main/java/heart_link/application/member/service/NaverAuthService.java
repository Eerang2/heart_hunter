package heart_link.application.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import heart_link.application.member.enums.Gender;
import heart_link.member.data.response.MemberRes;
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

    @Value("${naver.client.id}")
    private String CLIENT_ID;

    @Value("${naver.client.secret}")
    private String CLIENT_SECRET;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TIME_LIMIT = 10 * 60 * 1000;

    public MemberRes authenticate(String code, String state, HttpSession session) throws Exception {
        String savedState = (String) session.getAttribute("oauth_state");
        Long createdAt = (Long) session.getAttribute("oauth_state_created_at");

        if (!state.equals(savedState)) throw new IllegalStateException("CSRF 공격");
        if (System.currentTimeMillis() - createdAt > TIME_LIMIT) throw new IllegalStateException("시간 초과");

        String tokenUrl = UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("code", code)
                .queryParam("state", state)
                .queryParam("redirect_uri", "http://localhost:8080/naver/callback")
                .build()
                .encode()
                .toUriString();

        ResponseEntity<String> tokenResponse = restTemplate.getForEntity(tokenUrl, String.class);
        String accessToken = objectMapper.readTree(tokenResponse.getBody()).get("access_token").asText();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> profileResponse = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, String.class);

        JsonNode profile = objectMapper.readTree(profileResponse.getBody()).get("response");

        return MemberRes.of(
                profile.get("email").asText(),
                Gender.valueOf(profile.get("gender").asText()),
                profile.get("name").asText()
        );
    }
}
