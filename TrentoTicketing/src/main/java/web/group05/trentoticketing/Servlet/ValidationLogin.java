package web.group05.trentoticketing.Servlet;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Date;


public class ValidationLogin extends HttpServlet {
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
        // se esiste, invalido la sessione
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
            session.invalidate();
        }

        User user = null;
        // valido credenziali e ottengo lo user
        Statement statement = null;
        ResultSet results = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM UTENTE WHERE USERNAME = '" + request.getParameter("username") + "' AND PASSWORD = '" + request.getParameter("password") + "'";
            results = statement.executeQuery(query);
            while (results.next()) {
                user = new User(results.getString(1), results.getString(2), results.getDate(3),
                        results.getString(4), results.getString(5), results.getString(6),
                        results.getBoolean(8));
            }
        } catch (SQLException e) {
            throw new UnavailableException("ValidationLogin.doFilter(  ) SQLException: " + e.getMessage(  ));
        }

        if (user == null) { // creadenziali errate
            request.getRequestDispatcher("./login.html").include(request, response);
        } else {
            session = request.getSession();
            session.setAttribute("user", user);
            request.getRequestDispatcher("./index.html").forward(request, response);
        }
    }

    // non metto la get dato che passo dati sensibili
}
