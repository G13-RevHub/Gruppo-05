package web.group05.trentoticketing.Servlet.Handler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ChangeEventSale", value = "/ChangeEventSale")
public class ChangeEventSale extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int event_id = Integer.parseInt(request.getParameter("id"));
        int sale = Integer.parseInt(request.getParameter("sale"));

        try {
            Statement statement = connection.createStatement();
            String query = "UPDATE EVENTO SET SALE = " + sale + " WHERE ID = " + event_id;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("ChangeEventSale.doPost() SQLException: " + e.getMessage(  ));
            //throw new UnavailableException("ChangeEventSale.doPost() SQLException: " + e.getMessage(  ));
        }

        request.getRequestDispatcher("HandleEvents").forward(request, response);
    }
}
