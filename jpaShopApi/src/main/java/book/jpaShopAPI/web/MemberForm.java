package book.jpaShopAPI.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    // 목적: 필드가 null이 아니고 빈 문자열이 아닌지 검사합니다.
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name; // name 필드가 null이거나 빈 문자열이면 오류 메시지를 반환합니다.
    private String city;
    private String street;
    private String zipcode;

}