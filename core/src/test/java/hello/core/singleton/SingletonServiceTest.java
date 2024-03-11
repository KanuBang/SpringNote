package hello.core.singleton;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
class SingletonServiceTest {
    @Test
    @DisplayName("싱글톤 패턴")
    public void test() {
        //의존 관계 상 클라이언트가 구체 클래스를 의존하게 됨.
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singleton Service1 " + singletonService1);
        System.out.println("singleton Service2 " + singletonService2);

        assertThat(singletonService1).isEqualTo(singletonService2);
        singletonService1.logic();
    }
}