package springbook.temp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class HelloController implements Controller{
  @Autowired
  HelloSpring helloSpring;
  
  public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res)
      throws Exception {
    // 1.사용자 요청해석
    String name = req.getParameter("name");
    
    // 2.비즈니스 로직 호출
    String message = this.helloSpring.sayHello(name);
    
    // 3. 모델 정보 생성
    Map<String, Object> model = new HashMap<String,Object>();
    model.put("message", message);  
    
    return new ModelAndView("/WEB-INF/view/hello.jsp", model);
  }

}
