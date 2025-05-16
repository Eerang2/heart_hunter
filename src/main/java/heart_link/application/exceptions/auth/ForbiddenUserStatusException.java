package heart_link.application.exceptions.auth;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * 인증되지않은 유저
 */
@ResponseStatus(HttpStatus.FORBIDDEN)   // 403 ERROR
public class ForbiddenUserStatusException extends BaseException {
    @Serial
    private static final long serialVersionUID = 202504300001L;
    public ForbiddenUserStatusException(ErrorCode errorCode) {
        super(errorCode);

    }
}
