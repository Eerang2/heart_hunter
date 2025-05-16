package heart_link.application.exceptions.auth;


import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * 회원이 인증을 하지않았을때 401 ERROR 발생
 *
 * @author 이진우
 * @since 2025.4.22
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 ERROR
public class UnverifiedAccountException extends BaseException {

    @Serial
    private static final long serialVersionUID = 202504300001L;
    public UnverifiedAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
