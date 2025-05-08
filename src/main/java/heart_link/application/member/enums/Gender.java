package heart_link.application.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    M("님성"), F("여성"), BOTH("양성");
    private final String gender;
}
