import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

/**
 * a hello world to example for CDI
 */
public class MainServlet extends HttpServlet {

    @Inject
    Hello hello;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(saySomething());
        out.close();
    }

    public String saySomething() {
        return hello.sayHelloWorld();
    }
}