package heart_link.application.exceptions.social;

import heart_link.application.exceptions.base.BaseException;
import heart_link.application.exceptions.enums.ErrorCode;

/**
 * CSRF 공격 의심
 */
public class CSRFAttackDetectedException extends BaseException {
    public CSRFAttackDetectedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
