package web.group05.trentoticketing.Servlet.Handler;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "CreateEventHandler", value = "/CreateEventHandler")
public class CreateEventHandler extends HttpServlet {
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
        Statement statement = null;
        ResultSet results = null;

        try {
            statement = connection.createStatement();

            String query = "SELECT MAX(ID) FROM EVENTO";
            results = statement.executeQuery(query);
            int id = 0;
            while (results.next()) {
                id = results.getInt(1) + 1;
            }

            query = "INSERT INTO EVENTO (ID, NOME, DATA, ORA, TIPO, LUOGO, PREZZO_BIGLIETTO, TIPO_BIGLIETTO) VALUES (" +
                    id + ", " +
                    "'" + request.getParameter("nome") + "', " +
                    "'" + request.getParameter("data") + "', " +
                    "'" + request.getParameter("ora") + "', " +
                    "" + request.getParameter("tipo") + ", " +
                    "" + request.getParameter("luogo") + ", " +
                    "" + request.getParameter("prezzo") + ", " +
                    request.getParameter("tipo_biglietto") +
                    ")";
            System.out.println(query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("CreateEventHandler.doPost() SQLException: " + e.getMessage(  ));
            //throw new UnavailableException("CreateEventHandler.doPost() SQLException: " + e.getMessage(  ));
        }

        request.getRequestDispatcher("HandleEvents").forward(request, response);
        //response.sendRedirect("HandleEvents");
    }

    // non metto la get
}
