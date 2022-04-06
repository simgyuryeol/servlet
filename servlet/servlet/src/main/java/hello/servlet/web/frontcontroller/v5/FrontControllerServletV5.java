package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String,Object> handlerMappinMap = new HashMap<>(); //아무 컨트롤러든 다 들어가야 함으로 object를 넣음
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappinMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappinMap() {
        handlerMappinMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3()); //왼쪽이 실행되면 오른쪽이 실행됨
        handlerMappinMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappinMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //v4 추가
        handlerMappinMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4()); //왼쪽이 실행되면 오른쪽이 실행됨
        handlerMappinMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappinMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object handler = getHandler(request);
        if (handler== null) {//예외처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler); //핸들러 어댑터 찾아오기
        ModelView mv = adapter.handle(request,response,handler); //모델뷰 반환

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(),request,response);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {

        for (MyHandlerAdapter adapter : handlerAdapters) { //iter를 쓰면 for문으로 바꿔줌
            if(adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다 handler="+handler);

    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Object handler = handlerMappinMap.get(requestURI);
        return handler;
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
