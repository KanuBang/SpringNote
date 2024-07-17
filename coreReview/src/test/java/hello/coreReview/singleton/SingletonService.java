package hello.coreReview.singleton;

public class SingletonService {

    private static SingletonService singletonService = new SingletonService();

    public static SingletonService getInstance() {
        return singletonService;
    }
    private SingletonService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
