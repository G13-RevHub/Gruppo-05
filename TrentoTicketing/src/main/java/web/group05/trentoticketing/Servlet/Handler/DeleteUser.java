package web.group05.trentoticketing.Servlet.Handler;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@WebServlet(name = "DeleteUser", value = "/DeleteUser")
public class DeleteUser extends HttpServlet {
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
            throw new UnavailableException("Class error from ValidationLogin\nClassNotFoundException: " + e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(URL, user, password);
        }
        catch (SQLException e) {
            throw new UnavailableException("Unable to establish the connection from ValidationLogin\nSQLException: " + e.getMessage(  ));
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");

        if (!Objects.equals(user.getUsername(), request.getParameter("username"))) {
            request.getRequestDispatcher("index.html").forward(request, response);
        } else {
            Statement statement = null;

            try {
                statement = connection.createStatement();
                String query = "DELETE FROM UTENTE WHERE USERNAME = '" + request.getParameter("username") + "'";
                System.out.println(query);
                statement.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println("DeleteUser.doPost() SQLException: " + e.getMessage());
                //throw new UnavailableException("DeleteUser.doPost() SQLException: " + e.getMessage());
            }

            request.getRequestDispatcher("Logout").forward(request, response);
        }
    }
}
