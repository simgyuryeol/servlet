package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="helloServlet", urlPatterns = "/hello") ///hello로 오면 이게 실행됨, 서블릿 이름과 url 맵핑은 겹치면 안됨
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("usernames");
        System.out.println("usernames = " + username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8"); //헤더 정보에 들어감
        response.getWriter().write("hello "+username);//http 메시지 바디에 데이터가 들어감

    }
}
