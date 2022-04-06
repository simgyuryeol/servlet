package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FrontControllerServletV4", urlPatterns = "/front-controller/v4/*") //v1하위의 어떤것이 오든 이것이 호출된다
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();


    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4()); //왼쪽이 실행되면 오른쪽이 실행됨
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        ControllerV4 controller = controllerMap.get(requestURI); //MAP에서 해당 값을 찾음, 키를 넣으면 해당하는 값이 나온다
        //ex) "/front-controller/v1/members" URI를 넣으면 new MemberListControllerV1() 객체가 반환
        // ControllerV1 controller = new MemberListControllerV1() 이렇게 된다.
        if (controller== null) {//예외처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //paramMap을 넘겨줌
        Map<String, String> pramMap = createParamMap(request);
        Map<String,Object> model = new HashMap<>();
        String viewname = controller.process(pramMap, model);//오버라이드된 해당 인터페이스를 호출
//mv.getViewName()으로 논리이름 new-form까지만 얻을 수 있다 이를 물리 주소로 바꿔야 한다.

        MyView view = viewResolver(viewname);

        view.render(model,request,response);
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
