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

@WebServlet(name = "EventPage", value = "/EventPage")
public class EventPage extends HttpServlet {
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
        int event_id = Integer.parseInt(request.getParameter("id"));

        Statement statement = null;
        ResultSet results = null;
        Event event = null;

        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM EVENTO WHERE ID = " + event_id;
            results = statement.executeQuery(query);
            while (results.next()) {
                event = new Event(results.getInt("ID"), results.getString("NOME"), results.getDate("DATA"),
                        results.getTime("ORA"), Event_Type.values()[results.getInt("TIPO")],
                        Event_Location.values()[results.getInt("LUOGO")], results.getBoolean("BIGLIETTO_POLTRONA"),
                        results.getDouble("PREZZO_BPOLTRONA"), results.getBoolean("BIGLIETTO_PIEDI"),
                        results.getDouble("PREZZO_BPIEDI"), results.getInt("BIGLIETTI_VENDUTI"), results.getInt("SALE"));
            }
        } catch (SQLException e) {
            System.out.println("EventPage.doGet() SQLException: " + e.getMessage());
            //throw new UnavailableException("EventPage.doGet() SQLException: " + e.getMessage());
        }

        if (event == null) {
            request.getRequestDispatcher("Home").forward(request, response);
            return;
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Evento",
                    ".main-div { height: 100%; padding: 10px; display: flex; flex-direction: row; }" +
                            ".img-div { height: 100%; padding: 10px; width: 25%; border-right: 1px solid black; display: flex; justify-content: center; align-items: center; }" +
                            ".data-div { height: 100%; padding: 10px; width: 75%; border-left: 1px solid black; display: flex; flex-direction: column; }" +
                            ".td1 { width: 50%; text-align: right; font-weight: bold; }" +
                            ".td2 { width: 50%; }" +
                            ".ticket-table { width: 500px; }" +
                            ".ticket-table tr th { border: 1px solid black; text-align: center; }" +
                            ".ticket-table tr td { border: 1px solid black; text-align: center; }" +
                            ""));

            out.println("<h1>Evento " + event.getName() + "</h1>");
            if (event.getSale() > 0) {
                out.println("<h3>È attivo uno sconto del " + event.getSale() + "% per questo evento!</h3>");
            }
            out.println("<div class='main-div'>" +
                    "<div class='img-div'>" +
                    "<img src='images/default.png' />" +
                    "</div>" +
                    "<div class='data-div'>" +
                    "<p>Dettagli sull'evento:</p>" +
                    "<table>" +
                    "<tr><td class='td1'>Data</td><td class='td2'>" + event.getDate() + "</td></tr>" +
                    "<tr><td class='td1'>Ora</td><td class='td2'>" + event.getTime() + "</td></tr>" +
                    "<tr><td class='td1'>Luogo</td><td class='td2'>" + Event.EventLocationToString(event.getLocation()) + "</td></tr>" +
                    "</table><br/>");
            if (event.getPoltronaTicket() == -1 && event.getPiediTicket() == -1) {
                out.println("<p>Non sono disponibili biglietti per l'evento</p>");
            } else {
                out.println("<p>Acquista i biglietti:</p>" +
                        "<table class='ticket-table'>" +
                        "<thead><tr><th>Tipo</th><th>Prezzo</th><th></th></tr>");
                if (event.getPoltronaTicket() != -1) {
                    out.println("<tr><td>Poltrona</td><td><p style='margin: 0px;'>" + (event.getSale() > 0 ? ("<s>" + event.getPoltronaTicket() + "</s> -> " + event.getPoltronaTicket() * (100 - event.getSale()) / 100) : event.getPoltronaTicket()) + " €</p></td><td><button class='btn btn-success' onclick='addToCart(1)'>+</button></td></tr>");
                }
                if (event.getPiediTicket() != -1) {
                    out.println("<tr><td>Piedi</td><td><p style='margin: 0px;'>" + (event.getSale() > 0 ? ("<s>" + event.getPiediTicket() + "</s> -> " + event.getPiediTicket() * (100 - event.getSale()) / 100) : event.getPiediTicket()) + " €</p></td><td><button class='btn btn-success' onclick='addToCart(2)'>+</button></td></tr>");
                }
                out.println("</table>");
            }
            out.println("</div></div>");

            out.println("<form id=\"add-form\" method=\"post\" action=\"AddToCart\" style=\"display: none;\">" +
                    "<input type=\"hidden\" name=\"event_id\" id=\"item-event_id\" value='" + event_id + "'>" +
                    "<input type=\"hidden\" name=\"ticket_type\" id=\"item-ticket_type\">" +
                    "</form>");

            out.println(HtmlHelper.getFooter("function addToCart(ticketType) {" +
                    "   document.getElementById('item-ticket_type').value = ticketType;" +
                    "   document.getElementById('add-form').submit();}"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
