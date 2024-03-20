package hello.core.lifestyle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient implements InitializingBean, DisposableBean {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시에 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료 시에 호출
    public void disConnect() {
        System.out.println("close: " + url);
    }

    // `InitializingBean`은 `afterPropertiesSet()` 메서드로 초기화를 지원한다.
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("초기화 콜백 발동 완료");
        connect();
        call("초기화 연결 메시지");
    }

    // `DisposableBean` 은 `destroy()` 메서드로 소멸을 지원한다.
    @Override
    public void destroy() throws Exception {
        disConnect();
    }


}
