package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username,age);
        response.getWriter().write("ok");
    }

    // @RequestParam: 파라미터 이름으로 바인딩
    // @ResponseBody: View 조회를 무시하고, Http message body에 직접 해당 내용을 입력
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName,memberAge);
        return "ok";
    }

    // HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx")` 생략 가능
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        log.info("username={}, age={}", username,age);
        return "ok";
    }

    // String,int,Integer 등의 단순 타입이면 @RequestParam도 생략 가능
    // `@RequestParam` 애노테이션을 생략하면 스프링 MVC는 내부에서 `required=false` 를 적용한다.
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username,age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(name="username", required = true) String username, @RequestParam(name = "age", required = false) Integer age) {
        log.info("username={}, age={}", username,age);
        return "ok";
    }

    // 기본값 지정, http 파라미터를 지정하지 않거나 공백이 들어올 경우 기본값 할당
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(name = "username", required = true, defaultValue = "guest") String username,
            @RequestParam(name = "age", required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username,age);
        return "ok";
    }

    // 파라미터 map 으로 조회하기
    // MultiValueMap 파라미터의 값이 1개 아닐 경우에 사용
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam MultiValueMap<String,Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
}
