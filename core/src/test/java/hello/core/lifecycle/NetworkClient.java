package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient {

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


    public void init() {
        System.out.println("init");
        connect();
        call("초기화 메시지");
    }

    public void close (){
        System.out.println("close ");
        disconnect();
    }

}

