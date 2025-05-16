package heart_link.application.exceptions.auth;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 유효하지않은 데이터일때
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // 404 ERROR
public class InvalidDataException extends BaseException {
    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
    }
}
