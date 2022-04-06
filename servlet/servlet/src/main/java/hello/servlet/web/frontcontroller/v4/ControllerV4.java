package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    /**
     *
     * @param paramap
     * @param model
     * @return viewName
     */
    String process(Map<String, String>paramap, Map<String,Object> model);
}
