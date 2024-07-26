package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @GetMapping
    public String getUsers(){
        return "get users";
    }

    @PostMapping
    public String addUser() {
        return "add user";
    }

    @GetMapping("/{userId}")
    public String findUser(@PathVariable("userId") String userId){
        logger.info("userId={}",userId);
        return "get userId = "+userId;
    }

    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable("userId") String userId){
        logger.info("userId={}",userId);
        return "update userId = "+userId;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") String userId){
        logger.info("userId={}",userId);
        return "delete userId = " + userId;
    }
}
