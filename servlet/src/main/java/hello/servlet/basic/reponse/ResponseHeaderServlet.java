package hello.servlet.basic.reponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // status - line
        response.setStatus(HttpServletResponse.SC_OK);

        // response-header
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        //헤더 편의 메서드
        content(response);
        cooklie(response);
        redirect(response);

        PrintWriter writer = response.getWriter();
        writer.write("RESPONSE OK");
    }

    private static void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
    }

    private static void cooklie(HttpServletResponse response) {
        //response.setHeader("Set-Cookie", "myCookie-good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);// 600초
        response.addCookie(cookie);
    }

    private static void content(HttpServletResponse response) {
        // response.setHeader("Content-Type", "text/plain;charset=utf-8");
        System.out.println("[content 편의 메서드]");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        // 생략 시 자동 생성 response.setContentLength(2);
    }
}
