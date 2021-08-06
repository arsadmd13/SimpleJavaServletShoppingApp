import java.io.*;

import javax.servlet.http.*;

public class LogoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("do-auth");
    }

    public void destroy() {
    }
}