package web.group05.trentoticketing.Servlet.Account;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;


@WebServlet(name = "ValidationLogin", value = "/ValidationLogin")
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
        HttpSession session = request.getSession(true);
        if (session != null) {
            session.removeAttribute("user");
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
                user = new User(results.getString("NOME"), results.getString("COGNOME"), results.getDate("DATA_NASCITA"),
                        results.getString("EMAIL"), results.getString("TELEFONO"), results.getString("USERNAME"),
                        results.getBoolean("IS_ADMIN"), results.getInt("BIGLIETTI_ACQUISTATI"));
            }
        } catch (SQLException e) {
            System.out.println("ValidationLogin.doPost() SQLException: " + e.getMessage());
            //throw new UnavailableException("ValidationLogin.doPost() SQLException: " + e.getMessage());
            request.getRequestDispatcher("account/login_failed.html").forward(request, response);
        }

        if (user == null) { // creadenziali errate
            //request.getRequestDispatcher("./account/login_failed.html").include(request, response);
            request.getRequestDispatcher("account/login_failed.html").forward(request, response);
        } else {
            session = request.getSession(true);
            session.setAttribute("user", user);
            request.getRequestDispatcher("Home").forward(request, response);
            // gestione caso cookie disabilitati
            //String s = response.encodeRedirectURL("Home");
            //response.sendRedirect(s);
            //request.getRequestDispatcher(response.encodeRedirectURL("./destination.html")).forward(request, response); // nelle altre pagine
        }
    }

    // non metto la get dato che passo dati sensibili
}
