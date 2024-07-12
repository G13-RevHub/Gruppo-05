package web.group05.trentoticketing.Servlet.Handler;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

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

            query = "INSERT INTO EVENTO (ID, NOME, DATA, TIPO, ORA, LUOGO, BIGLIETTO_POLTRONA, BIGLIETTO_PIEDI, PREZZO_BPOLTRONA, PREZZO_BPIEDI) VALUES (" +
                    id + ", " +
                    "'" + request.getParameter("nome") + "', " +
                    "'" + request.getParameter("data") + "', " +
                    request.getParameter("tipo") + ", " +
                    "'" + request.getParameter("ora") + "', " +
                    "" + request.getParameter("luogo") + ", " +
                    "" + (Objects.equals(request.getParameter("poltrona"), "on") ? "true" : "false") + ", " +
                    "" + (Objects.equals(request.getParameter("piedi"), "on") ? "true" : "false") + ", " +
                    "" + request.getParameter("prezzo-poltrona") + ", " +
                    request.getParameter("prezzo-piedi") +
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
