package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient implements InitializingBean, DisposableBean {

    private String url;


    public NetworkClient() {
        System.out.println("생성자 호출, url = ");
        connect();
        call("초기화 연결 메시지");
    }

    public void call(String msg) {
        System.out.println("call: " + url + " msg: " + msg);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect() {
        System.out.println("Connect: " + url);
    }

    public void disconnect() {
        System.out.println("Disonnect: " + url);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 메시지");
    }

    @Override
    public void destroy() throws Exception {
        disconnect();
    }


}

