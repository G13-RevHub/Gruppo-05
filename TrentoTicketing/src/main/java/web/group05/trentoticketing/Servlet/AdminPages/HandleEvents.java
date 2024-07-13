package web.group05.trentoticketing.Servlet.AdminPages;

import web.group05.trentoticketing.Data.Enums.Event_Location;
import web.group05.trentoticketing.Data.Enums.Event_Type;
import web.group05.trentoticketing.Data.Event;
import web.group05.trentoticketing.Data.Ticket;
import web.group05.trentoticketing.Data.User;
import web.group05.trentoticketing.Helper.HtmlHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;


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
        HttpSession session = request.getSession(false);

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
                        Event_Location.values()[results.getInt("LUOGO")], results.getBoolean("BIGLIETTO_POLTRONA"),
                        results.getDouble("PREZZO_BPOLTRONA"), results.getBoolean("BIGLIETTO_PIEDI"),
                        results.getDouble("PREZZO_BPIEDI"), results.getInt("BIGLIETTI_VENDUTI"), results.getInt("SALE")));
            }
        } catch (SQLException e) {
            System.out.println("HandleEvent.doGet() SQLException: " + e.getMessage());
            //throw new UnavailableException("HandleEvent.doGet() SQLException: " + e.getMessage());
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Eventi",
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

            out.println("<h1>Eventi</h1>");
            out.println("<a class=\"btn btn-success mb-1 mx-4\" href=\"CreateEvent\">Crea un evento</a>");
            if (events.size() == 0) {
                out.println("<p>Nessun evento presente al momento</p>");
            } else {
                out.println("<label for=\"sortCheckbox\">Attiva/Disattiva ordinamento</label><input type=\"checkbox\" id=\"sortCheckbox\">");
                out.println("<table id=\"eventsTable\"><thead><tr><th>Nome</th><th>Data</th><th>Ora</th><th>Tipo</th><th>Luogo</th><th>Ticket Poltrona</th><th>Ticket In Piedi</th><th>Biglietti Venduti</th><th>Sconto</th><th></th><th></th></tr></thead><tbody>");
                for (Event e: events) {
                    out.println("<tr>");
                    out.println("<td>" + e.getName() + "</td>");
                    out.println("<td>" + e.getDate() + "</td>");
                    out.println("<td>" + e.getTime() + "</td>");
                    out.println("<td>" + Event.EventTypeToString(e.getType()) + "</td>");
                    out.println("<td>" + Event.EventLocationToString(e.getLocation()) + "</td>");
                    out.println("<td>" + ((e.getPoltronaTicket() == -1) ? "-" : e.getPoltronaTicket() + " €") + "</td>");
                    out.println("<td>" + ((e.getPiediTicket() == -1) ? "-" : e.getPiediTicket() + " €") + "</td>");
                    out.println("<td>" + e.getTickets_sold() + "</td>");
                    out.println("<td><input type=\"number\" min=\"0\" max=\"100\" step=\"1\" id=\"sale-" + e.getId() + "\" value=\"" + e.getSale() + "\" /></td>");
                    out.println("<td><button class=\"btn btn-primary\" onclick=\"setSale(" + e.getId() + ")\">Aggiorna</button></td>");
                    out.println("<td><button class=\"btn btn-danger\" onclick=\"deleteItem(" + e.getId() + ")\">Elimina</button></td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table>");

                out.println("<script>\n" +
                        "        let originalOrder = [];\n" +
                        "        document.addEventListener(\"DOMContentLoaded\", function() {\n" +
                        "            const tbody = document.querySelector(\"#eventsTable tbody\");\n" +
                        "            originalOrder = Array.from(tbody.querySelectorAll(\"tr\"));\n" +
                        "        });\n" +
                        "        document.getElementById(\"sortCheckbox\").addEventListener(\"change\", function() {\n" +
                        "            if (this.checked) {\n" +
                        "                sortTableByColumn(\"eventsTable\", 7, true);\n" +
                        "            } else {\n" +
                        "                resetTableOrder(\"eventsTable\");\n" +
                        "            }\n" +
                        "        });\n" +
                        "        function sortTableByColumn(tableId, columnIndex, desc = false) {\n" +
                        "            const table = document.getElementById(tableId);\n" +
                        "            const tbody = table.querySelector(\"tbody\");\n" +
                        "            const rows = Array.from(tbody.querySelectorAll(\"tr\"));\n" +
                        "            const sortedRows = rows.sort((a, b) => {\n" +
                        "                const aColText = a.querySelector(`td:nth-child(${columnIndex + 1})`).textContent.trim();\n" +
                        "                const bColText = b.querySelector(`td:nth-child(${columnIndex + 1})`).textContent.trim();\n" +
                        "                const aColValue = parseInt(aColText, 10);\n" +
                        "                const bColValue = parseInt(bColText, 10);\n" +
                        "                return desc ? bColValue - aColValue : aColValue - bColValue;\n" +
                        "            });\n" +
                        "            while (tbody.firstChild) {\n" +
                        "                tbody.removeChild(tbody.firstChild);\n" +
                        "            }\n" +
                        "            tbody.append(...sortedRows);\n" +
                        "        }\n" +
                        "        function resetTableOrder(tableId) {\n" +
                        "            const table = document.getElementById(tableId);\n" +
                        "            const tbody = table.querySelector(\"tbody\");\n" +
                        "            while (tbody.firstChild) {\n" +
                        "                tbody.removeChild(tbody.firstChild);\n" +
                        "            }\n" +
                        "            tbody.append(...originalOrder);\n" +
                        "        }\n" +
                        "    </script>");
            }
            out.println("<form id=\"delete-form\" method=\"post\" action=\"DeleteEvent\" style=\"display: none;\">" +
                    "<input type=\"hidden\" name=\"id\" id=\"delete-item-id\">" +
                    "</form>" +
                    "<form id=\"sale-form\" method=\"post\" action=\"ChangeEventSale\" style=\"display: none;\">" +
                    "<input type=\"hidden\" name=\"id\" id=\"sale-item-id\">" +
                    "<input type=\"hidden\" name=\"sale\" id=\"sale\">" +
                    "</form>");
            out.println("<script>" +
                    "function deleteItem(itemId) {" +
                    "   document.getElementById('delete-item-id').value = itemId;" +
                    "   document.getElementById('delete-form').submit();}" +
                    "function setSale(itemId) {" +
                    "   document.getElementById('sale-item-id').value = itemId;" +
                    "   document.getElementById('sale').value = document.getElementById(`sale-${itemId}`).value;" +
                    "   document.getElementById('sale-form').submit();}" +
                    "</script>");

            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
