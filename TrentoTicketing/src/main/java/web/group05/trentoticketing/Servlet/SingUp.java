package web.group05.trentoticketing.Servlet;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Date;


public class SingUp extends HttpServlet {
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
        // se esiste, invalido la sessione
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();

        // creo lo user nel DB
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query = "INSERT INTO UTENTE (NOME, COGNOME, DATA_NASCITA, EMAIL, TELEFONO, USERNAME, PASSWORD) VALUES (" +
                    "'" + request.getParameter("nome") + "'" +
                    "'" + request.getParameter("cognome") + "'" +
                    "'" + request.getParameter("data_nascita") + "'" +
                    "'" + request.getParameter("email") + "'" +
                    "'" + request.getParameter("telefono") + "'" +
                    "'" + request.getParameter("username") + "'" +
                    "'" + request.getParameter("password") + "'" +
                    ")";
            statement.executeQuery(query);
        } catch (SQLException e) {
            throw new UnavailableException("SingUp.doFilter(  ) SQLException: " + e.getMessage(  ));
        }

        request.getRequestDispatcher("./login.html").include(request, response);
    }

    // non metto la get dato che passo dati sensibili
}
