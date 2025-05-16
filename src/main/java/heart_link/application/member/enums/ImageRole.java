package heart_link.application.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageRole {
    MAIN("대표이미지"),SUB("서브이미지");
    private final String role;
}
