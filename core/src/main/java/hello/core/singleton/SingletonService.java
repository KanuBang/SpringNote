package hello.core.singleton;

public class SingletonService {

    //1. static 영역에 객체를 딱 1개만 생성해둔다.
    private static final SingletonService instance = new SingletonService();

    //2. public으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한다.
    public static SingletonService getInstance () {
        return instance;
    }

    //3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다.
    private SingletonService() {

    }
    //private 생성자이기 때문에 상속해서 자식을 만들기 어렵다.
    //결론적으로, 유연성이 떨어져 안티패턴으로 불리기도 한다.
    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
