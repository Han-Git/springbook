package springbook.temp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.ModelAndView;

public class ServletTest {
  @Test
  public void doTest() throws ServletException, IOException {
    ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
    System.out.println(getClass().toString());
    servlet.setRelativeLocations(getClass(), "spring-servlet.xml");
    servlet.setClasses(HelloSpring.class);
    
    servlet.init(new MockServletConfig("spring"));
    
    MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
    req.addParameter("name","Spring");
    MockHttpServletResponse res = new MockHttpServletResponse();
    
    servlet.service(req, res);
    
    ModelAndView mav = servlet.getModelAndView();
    assertThat(mav.getViewName(), is("/WEB-INF/view/hello.jsp"));
    assertThat((String)mav.getModel().get("message"), is("Hello Spring"));
    
  }
}
