package heart_link.application.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀에러에 대한 ENUM 클래스
 * 에러코드와 에러에 대한 메세지 설정
 *
 * @author 이진우
 * @since 2025.4.22
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {


    // 인증/인가 관련
    USER_NOT_FOUND("AUTH_001", "해당 유저를 찾을 수 없습니다."),
    INVALID_CREDENTIALS("AUTH_002", "아이디 또는 비밀번호가 일치하지 않습니다."),
    FORBIDDEN_USER_STATUS("AUTH004", "해당 계정은 사용할 수 없습니다. (삭제 또는 휴면 상태)"),
    VALID_IMAGE_COUNT("AUTH005", "이미지 조건에 알맞지 않습니다."),
    AUTH_CSRF_ATTACK_DETECTED("AUTH006", "CSRF 공격 의심됨"),
    AUTH_UNAUTHORIZED("AUTH007", "인증되지 않은 접근"),

    INVALID_REQUEST_BODY("COMMON_001", "요청 데이터가 비어있거나 유효하지 않습니다."),
    MISSING_REQUIRED_FIELDS("COMMON_002", "필수 입력 항목이 누락되었습니다."),
    EXPIRED_REFRESH_TOKEN("COMMON003", "만료된 토큰입니다."),
    UNAUTHORIZED_ACCESS("COMMON004", "인증되지 않은 접근입니다."),

    TIMEOUT_REQUEST("T001", "요청 시간이 초과되었습니다"),

    INQUIRY_NOT_FOUND("HELPDESK_001", "해당 고객센터 문의를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
