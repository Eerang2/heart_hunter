package heart_link.application.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType {

    SERVICE_USE("서비스이용"),
    PRIVACY("개인정보"),
    SENSITIVE_INFO("민감한정보"),
    LOCATION("위치"),
    OVER_14("14세이상"),
    MARKETING("마케팅수신");

    private final String description;
}
