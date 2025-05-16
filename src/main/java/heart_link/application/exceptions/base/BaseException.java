package heart_link.application.exceptions.base;

import heart_link.application.exceptions.enums.ErrorCode;
import lombok.Getter;

import java.io.Serial;


/**
 * runtimeException을 상속받아 errorCode enum 클래스를 받아야되는 Base 클래스
 *
 * @author 이진우
 * @since 2025.4.23
 */
@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 202504230001L;

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
