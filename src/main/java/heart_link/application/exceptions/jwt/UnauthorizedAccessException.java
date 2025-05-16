package heart_link.application.exceptions.jwt;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * 토큰이 DB 와 일치하지않을때
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 ERROR
public class UnauthorizedAccessException extends BaseException {

    @Serial
    private static final long serialVersionUID = 202504300001L;
    public UnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
