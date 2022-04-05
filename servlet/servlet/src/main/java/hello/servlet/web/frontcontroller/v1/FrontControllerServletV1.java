package hello.servlet.web.frontcontroller.v1;

import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;
import hello.servlet.web.servlet.MemberSaveServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FrontControllerServletV1", urlPatterns = "/front-controller/v1/*") //v1하위의 어떤것이 오든 이것이 호출된다
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerV1Map = new HashMap<>();


    public FrontControllerServletV1() {
        controllerV1Map.put("/front-controller/v1/members/new-form", new MemberFormControllerV1()); //왼쪽이 실행되면 오른쪽이 실행됨
        controllerV1Map.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerV1Map.put("/front-controller/v1/members", new MemberListControllerV1());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");;

        String requestURI = request.getRequestURI();

        ControllerV1 controller = controllerV1Map.get(requestURI); //MAP에서 해당 값을 찾음, 키를 넣으면 해당하는 값이 나온다
        //ex) "/front-controller/v1/members" URI를 넣으면 new MemberListControllerV1() 객체가 반환
        // ControllerV1 controller = new MemberListControllerV1() 이렇게 된다.
        if (controller== null) {//예외처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        controller.process(request,response); //오버라이드된 해당 인터페이스를 호출

    }
}
