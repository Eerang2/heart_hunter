package heart_link.application.exceptions.auth;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * 유저가 DB의 정보와 일치하지않을때
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // 404 ERROR
public class UserNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = 202504300001L;
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
