package springbook.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import springbook.temp.SimpleGetServlet;

public class MockSimpleGetServlet {
  
  @Test
  public void mockDoGet() throws Exception {
    MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
    req.addParameter("name", "Spring");
    
    MockHttpServletResponse res = new MockHttpServletResponse();
    
    SimpleGetServlet servlet = new SimpleGetServlet();
    servlet.service(req, res);
    
    assertThat(res.getContentAsString(),is("<HTML><BODY>Hello Spring</BODY></HTML>"));
    
  }
}
