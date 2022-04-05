package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FrontControllerServletV3", urlPatterns = "/front-controller/v3/*") //v1하위의 어떤것이 오든 이것이 호출된다
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();


    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3()); //왼쪽이 실행되면 오른쪽이 실행됨
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI); //MAP에서 해당 값을 찾음, 키를 넣으면 해당하는 값이 나온다
        //ex) "/front-controller/v1/members" URI를 넣으면 new MemberListControllerV1() 객체가 반환
        // ControllerV1 controller = new MemberListControllerV1() 이렇게 된다.
        if (controller== null) {//예외처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap을 넘겨줌
        Map<String, String> pramMap = createParamMap(request);
        ModelView mv = controller.process(pramMap);//오버라이드된 해당 인터페이스를 호출
        //mv.getViewName()으로 논리이름 new-form까지만 얻을 수 있다 이를 물리 주소로 바꿔야 한다.

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(),request,response);

    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) { //메소드 뽑기 컨트롤+알트+M
        Map<String,String> pramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> pramMap.put(paramName, request.getParameter(paramName)));
        return pramMap;
    }
}
