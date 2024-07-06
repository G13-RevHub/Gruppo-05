package web.group05.trentoticketing.Servlet.AutenticatedPages;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "UserProfile", value = "/UserProfile")
public class UserProfile extends HttpServlet {
    Connection connection = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        String URL = "jdbc:derby://localhost:1527/TrentoTicketing";
        String user = "App";
        String password = "pw";
        super.init(config);

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Class error from SingUp\nClassNotFoundException: " + e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(URL, user, password);
        }
        catch (SQLException e) {
            throw new UnavailableException("Unable to establish the connection from SingUp\nSQLException: " + e.getMessage(  ));
        }
    }
    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head><title>Profilo</title>" +
                    "<style>" +
                    ".profile-table { width: 100%; }" +
                    ".cl1 { width: 50%; text-align: right; padding-right: 5px; }" +
                    ".cl2 { width: 50%; text-align: left; padding-left: 5px; }" +
                    ".delete-button {  }" +
                    "</style></head><body>");
            out.println("<h1>Profilo Utente</h1>" +
                    "<table class=\"profile-table\">" +
                    "<tr><td class=\"cl1\">Username</td><td class=\"cl2\">" + user.getUsername() + "</td></tr>" +
                    "<tr><td class=\"cl1\">Nome</td><td class=\"cl2\">" + user.getNome() + "</td></tr>" +
                    "<tr><td class=\"cl1\">Cognome</td><td class=\"cl2\">" + user.getCognome() + "</td></tr>" +
                    "<tr><td class=\"cl1\">Data di Nascita</td><td class=\"cl2\">" + user.getData_nascita() + "</td></tr>" +
                    "<tr><td class=\"cl1\">Email</td><td class=\"cl2\">" + user.getEmail() + "</td></tr>" +
                    "<tr><td class=\"cl1\">Telefono</td><td class=\"cl2\">" + user.getTelefono() + "</td></tr>" +
                    "</table>");
            out.println("<center><button class=\"delete-button\" onclick=\"deleteProfile()\">Elimina Profilo</button></center>");
            out.println("<form id=\"delete-form\" method=\"post\" action=\"DeleteUser\" style=\"display: none;\">" +
                    "<input type=\"hidden\" name=\"username\" id=\"user-username\" value=\"" + user.getUsername() + "\">" +
                    "</form>");
            out.println("<script>\n" +
                    "function deleteProfile() {\n" +
                    "   document.getElementById('delete-form').submit();\n}\n" +
                    "</script>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
