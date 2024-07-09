package web.group05.trentoticketing.Servlet;

import web.group05.trentoticketing.Data.User;
import web.group05.trentoticketing.Helper.HtmlHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Home", value = "/Home")
public class Home extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        User user = session != null ? (User)session.getAttribute("user") : null;

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(user, "Home"));

            out.println("<h1>TrentoTicketing</h1>\n" +
                    "        <img src=\"images/logo.png\" />\n" +
                    "        <br><br>\n" +
                    "\n" +
                    "        <div style=\"bottom: 0px; margin-top: 5px;\">\n" +
                    "            <table>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">Sede</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">Trento, via Roma 11</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">P. IVA</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">79846512345</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">Mail</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">info@trentoticketing.it</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">Telefono</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">389 154 3148</td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </div>");

            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
