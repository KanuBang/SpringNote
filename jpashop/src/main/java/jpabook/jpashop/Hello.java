package jpabook.jpashop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// lombok이 getter, setter 생성
@Getter @Setter
public class Hello {
    public String data;
}
