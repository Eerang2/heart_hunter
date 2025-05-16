package heart_link.application.exceptions.auth;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;

/**
 * 요청시간 초과
 */
public class TimeoutRequestException extends BaseException {
    public TimeoutRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
