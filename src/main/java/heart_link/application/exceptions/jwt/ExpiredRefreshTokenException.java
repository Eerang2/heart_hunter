package heart_link.application.exceptions.jwt;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * 만료된 리프레시토큰
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 ERROR
public class ExpiredRefreshTokenException extends BaseException {

    @Serial
    private static final long serialVersionUID = 202504300001L;
    public ExpiredRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
