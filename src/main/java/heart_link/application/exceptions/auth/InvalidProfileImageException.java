package heart_link.application.exceptions.auth;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 유저 프로필 이미지의 데이터가 유효하지않을떄
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // 404 ERROR
public class InvalidProfileImageException extends BaseException {
    public InvalidProfileImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
