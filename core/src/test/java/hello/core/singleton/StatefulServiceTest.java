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
        ApplicationContext ac = new AnnotationConfigApplicationContext(StatefulSearchService.class);

        // 두 변수가 같은 Bean을 공유하고 있음.
        StatefulSearchService searchService1 = ac.getBean(StatefulSearchService.class);
        StatefulSearchService searchService2 = ac.getBean(StatefulSearchService.class);


        //메시의 검색 내용
        searchService1.search("메시", "호날두의 천재성");

        //호날두의 검색 내용
        searchService2.search("호날두", "메시는 멍청이");

        //메시는 "호날두는 천재"라는 자신의 검색 기록을 확인할려고 했다.
        //하지만, 어쩌다가 호날두의 검색 기록을 확인하게 되었다.
        //싱글톤 객체가 stateful로 설계되었기 때문이다.
        Assertions.assertThat(searchService1.getrecord()).isEqualTo("메시는 멍청이");

    }
}
