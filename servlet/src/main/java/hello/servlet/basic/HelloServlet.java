package hello.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//콘트로 t -> 메서드 추출


// 서블릿 애노테이션 - 서블릿 이름, URL 매핑
@WebServlet(name= "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    // HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 다음 메서드를 실행한다.
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HelloServlet.class");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("username");
        System.out.println("username = " + username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        /*
        응답의 본문에 쓰기 -> 웹 브라우저에 값을 내려주고 쓰는 역할
         */
        response.getWriter().write("hello " + username);

    }
}
