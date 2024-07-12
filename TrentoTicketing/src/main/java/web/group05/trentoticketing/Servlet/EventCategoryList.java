package web.group05.trentoticketing.Servlet;

import web.group05.trentoticketing.Data.Enums.Event_Location;
import web.group05.trentoticketing.Data.Enums.Event_Type;
import web.group05.trentoticketing.Data.Event;
import web.group05.trentoticketing.Data.User;
import web.group05.trentoticketing.Helper.HtmlHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "EventCategoryList", value = "/EventCategoryList")
public class EventCategoryList extends HttpServlet {
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
        HttpSession session = request.getSession(true);
        User user = session != null ? (User)session.getAttribute("user") : null;
        int event_type = Integer.parseInt(request.getParameter("type"));

        Statement statement = null;
        ResultSet results = null;
        ArrayList<Event> events = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM EVENTO WHERE TIPO = " + event_type;
            results = statement.executeQuery(query);
            while (results.next()) {
                events.add(new Event(results.getInt("ID"), results.getString("NOME"), results.getDate("DATA"),
                        results.getTime("ORA"), Event_Type.values()[results.getInt("TIPO")],
                        Event_Location.values()[results.getInt("LUOGO")], results.getBoolean("BIGLIETTO_POLTRONA"),
                        results.getDouble("PREZZO_BPOLTRONA"), results.getBoolean("BIGLIETTO_PIEDI"),
                        results.getDouble("PREZZO_BPIEDI"), results.getInt("BIGLIETTI_VENDUTI"), results.getInt("SALE")));
            }
        } catch (SQLException e) {
            System.out.println("EventCategoryList.doGet() SQLException: " + e.getMessage());
            //throw new UnavailableException("EventCategoryList.doGet() SQLException: " + e.getMessage());
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Lista Eventi",
                    "table {\n" +
                            "            width: 100%;\n" +
                            "            border-collapse: collapse;\n" +
                            "        }\n" +
                            "thead { font-weight: bold; }" +
                            "        th, td {\n" +
                            "            padding: 10px;\n" +
                            "            text-align: center;\n" +
                            "            border: 1px solid #ddd;\n" +
                            "        }"));

            out.println("<h1>Eventi - " + Event_Type.values()[event_type] + "</h1>");

            if (events.size() > 0) {
                out.println("<table><thead><tr><td>Nome</td><td>Data</td><td>Ora</td><td>Luogo</td><td></td></tr></thead><tbody>");
                for (Event e: events) {
                    out.println("<tr>");
                    out.println("<td>" + e.getName() + "</td>");
                    out.println("<td>" + e.getDate() + "</td>");
                    out.println("<td>" + e.getTime() + "</td>");
                    out.println("<td>" + Event.EventLocationToString(e.getLocation()) + "</td>");
                    out.println("<td><a class='btn btn-secondary' href='EventPage?id=" + e.getId() + "'>Apri</a></td>");
                    //out.println("<td>" + (e.getSale() != 0 ? e.getSale() + "%" : "") + "</td>");
                    //out.println("<td>" + ((e.getPoltronaTicket() == -1) ? "" : e.getPoltronaTicket() + " € <button class=\"btn btn-success\" onclick=\"addToCart(" + e.getId() + ", 1)\">+</button>") + "</td>");
                    //out.println("<td>" + ((e.getPiediTicket() == -1) ? "" : e.getPiediTicket() + " € <button class=\"btn btn-success\" onclick=\"addToCart(" + e.getId() + ", 2)\">+</button>") + "</td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table>");
            } else {
                out.println("<p>Al momento non sono presenti eventi di questa categoria</p>");
            }

            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
