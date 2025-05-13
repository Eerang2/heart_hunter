package heart_link.infrastructure.util.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokensResponseDTO {

    private String accessToken;
    private String refreshToken;


    public static TokensResponseDTO of (String accessToken, String refreshToken) {
        return new TokensResponseDTO(accessToken, refreshToken);
    }
}
