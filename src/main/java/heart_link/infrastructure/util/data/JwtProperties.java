package heart_link.infrastructure.util.data;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtProperties {

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpireTime;
}
