package web.group05.trentoticketing.Servlet.AdminPages;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "SeeUsers", value = "/SeeUsers")
public class SeeUsers extends HttpServlet {
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
        Statement statement = null;
        ResultSet results = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT NOME, COGNOME, DATA_NASCITA, EMAIL, TELEFONO, USERNAME, BIGLIETTI_AQUISTATI FROM UTENTE WHERE IS_ADMIN = FALSE";
            results = statement.executeQuery(query);
            ArrayList<User> users = new ArrayList<>();
            while (results.next()) {
                users.add(new User(results.getString(1), results.getString(2), results.getDate(3),
                        results.getString(4), results.getString(5), results.getString(6),
                        false, results.getInt(9)));
            }
        } catch (SQLException e) {
            throw new UnavailableException("ValidationLogin.doFilter(  ) SQLException: " + e.getMessage(  ));
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
