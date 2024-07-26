package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello-basic")
    public String helloBasic() {
        logger.info("helloBasic");
        return "ok";
    }

    @GetMapping("/mapping-get-v2")
    public String mappingGetV2() {
        logger.info("mapping-get-v2");
        return "ok";
    }

    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data){
        logger.info("mappingPath userId = {}", data);
        return "ok";
    }

    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable("userId") String userId, @PathVariable("orderId") String orderId) {
        logger.info("mappingPath userId = {}, orderId = {}", userId, orderId);
        return "ok";
    }

    // 특정 파리미터 조건 매핑
    // http://localhost:8080/mapping-param?asia=korea
    @GetMapping(value = "/mapping-param", params = "africa=moroco")
    public String mappingParam() {
        logger.info("mappingParam");
        return "ok";
    }

    // 특정 헤더 조건 매핑
    @GetMapping(value = "/mapping-param", headers = "asia=japan")
    public String mappingHeader() {
        logger.info("mappingParam");
        return "ok";
    }

    // HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.
    // HTTP 415 상태코드(Unsupported Media Type)을 반환한다.
    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    public String mappingConsumes() {
        logger.info("mappingConsumes");
        return "ok";
    }

    @PostMapping(value = "/mapping-produce", produces = "text/html")
    public String mappingProduces() {
        logger.info("mappingProduces");
        return "ok";
    }
}
