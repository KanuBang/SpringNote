package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StatefulServiceTest {

    @Test
    @DisplayName("싱글톤 패턴은 stateless로 설계해야 한다.")
    public void statefulServiceTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(StatefulService.class);

        // 두 변수가 같은 Bean을 공유하고 있음.
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);


        //사용자 A의 정보
        statefulService1.order("방찬우", 10000);
        //사용자 B의 정보
        statefulService2.order("모드리치", 100000000);

        //사용자 A는 10000원을 기대했겠지만 실상 100000000이다.
        Assertions.assertThat(statefulService1).isNotEqualTo(10000);

    }
}
