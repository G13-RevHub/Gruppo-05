package web.group05.trentoticketing.Servlet.AdminPages;

import web.group05.trentoticketing.Data.User;
import web.group05.trentoticketing.Helper.HtmlHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
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
        HttpSession session = request.getSession(false);
        User user = session != null ? (User)session.getAttribute("user") : null;

        Statement statement = null;
        ResultSet results = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            statement = connection.createStatement();
            String query = "SELECT NOME, COGNOME, DATA_NASCITA, EMAIL, TELEFONO, USERNAME, IS_ADMIN, BIGLIETTI_ACQUISTATI FROM UTENTE WHERE IS_ADMIN = FALSE";
            results = statement.executeQuery(query);
            while (results.next()) {
                users.add(new User(results.getString("NOME"), results.getString("COGNOME"), results.getDate("DATA_NASCITA"),
                        results.getString("EMAIL"), results.getString("TELEFONO"), results.getString("USERNAME"),
                        results.getBoolean("IS_ADMIN"), results.getInt("BIGLIETTI_ACQUISTATI")));
            }
        } catch (SQLException e) {
            System.out.println("SeeUsers.doGet() SQLException: " + e.getMessage());
            throw new UnavailableException("SeeUsers.doGet() SQLException: " + e.getMessage());
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(user, "Utenti",
                    "table {\n" +
                    "            width: 100%;\n" +
                    "            border-collapse: collapse;\n" +
                    "        }\n" +
                            "thead { font-weight: bold; }" +
                    "        th, td {\n" +
                    "            padding: 10px;\n" +
                    "            text-align: left;\n" +
                    "            border: 1px solid #ddd;\n" +
                    "        }"));

            out.println("<h1>Utenti</h1>");
            if (users.size() == 0) {
                out.println("<p>Nessun utente registrato</p>");
            } else {
                out.println("<label>" +
                        "<input type=\"checkbox\" id=\"sort-checkbox\"> Ordina per biglietti acquistati" +
                        "</label>");
                out.println("<table><thead><tr><td>Nome</td><td>Cognome</td><td>Nascita</td><td>Email</td><td>Telefono</td><td>Username</td><td>Biglietti Acquistati</td></tr></thead><tbody>");
                for (User u: users) {
                    out.println("<tr>");
                    out.println("<td>" + u.getNome() + "</td>");
                    out.println("<td>" + u.getCognome() + "</td>");
                    out.println("<td>" + u.getData_nascita() + "</td>");
                    out.println("<td>" + u.getEmail() + "</td>");
                    out.println("<td>" + u.getTelefono() + "</td>");
                    out.println("<td>" + u.getUsername() + "</td>");
                    out.println("<td data-value=\"" + u.getBiglietti_aquistati() + "\">" + u.getBiglietti_aquistati() + "</td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table>");
            }
            out.println("<script>\n" +
                    "        document.getElementById('sort-checkbox').addEventListener('change', function () {\n" +
                    "            const table = document.getElementById('data-table');\n" +
                    "            const tbody = table.querySelector('tbody');\n" +
                    "            const rows = Array.from(tbody.querySelectorAll('tr'));\n" +
                    "            if (this.checked) {\n" +
                    "                rows.sort((a, b) => {\n" +
                    "                    const aValue = parseInt(a.querySelector('td[data-value]').dataset.value, 10);\n" +
                    "                    const bValue = parseInt(b.querySelector('td[data-value]').dataset.value, 10);\n" +
                    "                    return aValue - bValue;\n" +
                    "                });\n" +
                    "            } else {\n" +
                    "                rows.sort((a, b) => {\n" +
                    "                    return parseInt(a.querySelector('td[data-value]').dataset.value, 10) -\n" +
                    "                           parseInt(b.querySelector('td[data-value]').dataset.value, 10);\n" +
                    "                });\n}\n" +
                    "            while (tbody.firstChild) {\n" +
                    "                tbody.removeChild(tbody.firstChild);\n}\n" +
                    "            rows.forEach(row => tbody.appendChild(row));\n" +
                    "        });\n" +
                    "    </script>");

            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
