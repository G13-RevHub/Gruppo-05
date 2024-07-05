package web.group05.trentoticketing.Servlet.AdminPages;

import web.group05.trentoticketing.Data.Enums.Event_Location;
import web.group05.trentoticketing.Data.Enums.Event_Type;
import web.group05.trentoticketing.Data.Event;
import web.group05.trentoticketing.Data.Ticket;
import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


@WebServlet(name = "HandleEvents", value = "/HandleEvents")
public class HandleEvents extends HttpServlet {
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
        ArrayList<Event> events = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM EVENTO";
            results = statement.executeQuery(query);
            while (results.next()) {
                events.add(new Event(results.getInt("ID"), results.getString("NOME"), results.getDate("DATA"),
                        results.getTime("ORA"), Event_Type.values()[results.getInt("TIPO")],
                        Event_Location.values()[results.getInt("LUOGO")], new Ticket(results.getFloat("PREZZO_BIGLIETTO"),
                        Ticket.TicketType.values()[results.getInt("TIPO_BIGLIETTO")]), results.getInt("BIGLIETTI_VENDUTI")));
            }
        } catch (SQLException e) {
            System.out.println("HandleEvent.doGet() SQLException: " + e.getMessage());
            throw new UnavailableException("HandleEvent.doGet() SQLException: " + e.getMessage());
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head><title>Lista Eventi</title></head><body>");
            out.println("<h1>Eventi</h1>");
            out.println("<a href=\"CreateEvent\">Crea un evento</a>");
            if (events.size() == 0) {
                out.println("<p>Nessun evento presente al momento</p>");
            } else {
                out.println("<table><thead><tr><td>Nome</td><td>Data</td><td>Ora</td><td>Tipo</td><td>Luogo</td><td>Ticket</td><td>Biglietti Venduti</td></tr><td></td></thead><tbody>");
                for (Event e: events) {
                    out.println("<tr>");
                    out.println("<td>" + e.getName() + "</td>");
                    out.println("<td>" + e.getDate() + "</td>");
                    out.println("<td>" + e.getTime() + "</td>");
                    out.println("<td>" + Event.EventTypeToString(e.getType()) + "</td>");
                    out.println("<td>" + Event.EventLocationToString(e.getLocation()) + "</td>");
                    out.println("<td>" + e.getTicket().getPrice() + " € - " + Ticket.TicketTypeToString(e.getTicket().getType()) + "</td>");
                    out.println("<td>" + e.getTickets_sold() + "</td>");
                    out.println("<td><button>Elimina</button></td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table>");
            }
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
