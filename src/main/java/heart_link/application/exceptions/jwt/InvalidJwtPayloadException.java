package heart_link.application.exceptions.jwt;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * jwt관련 dto의 데이터가 비어있을때 404 ERROR 발생
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // 404 ERROR
public class InvalidJwtPayloadException extends BaseException {

    @Serial
    private static final long serialVersionUID = 202504300001L;
    public InvalidJwtPayloadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
