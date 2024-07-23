package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

        readAllParamters(request);
        readSingleParamter(request);
        readDuplicateParameter(request);

        response.getWriter().write("ok");
    }

    private static void readDuplicateParameter(HttpServletRequest request) {
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String username : usernames) {
            System.out.println("username: " + username);
        }
        System.out.println();
    }

    private static void readSingleParamter(HttpServletRequest request) {
        System.out.println("[단일 파라미터 조회]");
        System.out.println("username: " + request.getParameter("username"));
        System.out.println("age: " + request.getParameter("age"));
        System.out.println();
    }

    private static void readAllParamters(HttpServletRequest request) {
        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator().forEachRemaining(param ->
                System.out.println("paramName: " + request.getParameter(param)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();
    }
}
