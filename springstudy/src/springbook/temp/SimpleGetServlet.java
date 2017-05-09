package springbook.temp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SimpleGetServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    String name = req.getParameter("name");

    res.getWriter().print("<HTML><BODY>");
    res.getWriter().print("Hello " + name);
    res.getWriter().print("</BODY></HTML>");
  }

}
