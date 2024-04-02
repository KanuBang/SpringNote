package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    // model: 컨트롤러에서 view로 데이터를 넘길 때 사용
    // model의 정보를 리턴하는 html에 넘겨라


    // 8080/hello -> hello() 메서드 -> model에 정보를 담고 -> return hello.html에 넘긴다.
    public String hello(Model model) {
        model.addAttribute("data", "hello!");
        return "hello";
    }
}
