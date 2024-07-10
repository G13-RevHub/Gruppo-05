package web.group05.trentoticketing.Servlet;

import web.group05.trentoticketing.Data.CartItem;
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

@WebServlet(name = "Cart", value = "/Cart")
public class Cart extends HttpServlet {
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
        User user = (User)session.getAttribute("user");
        Object cartObj = session.getAttribute("cart");
        CartItem[] cart = (CartItem[])cartObj;
        ArrayList<String> eventsOnSale = new ArrayList<>();

        if (cart != null) {
            Statement statement = null;
            ResultSet results = null;
            try {
                statement = connection.createStatement();

                for (CartItem item: cart) {
                    String query = "SELECT * FROM EVENTO WHERE ID = " + item.id;
                    results = statement.executeQuery(query);
                    while (results.next()) {
                        item.event = new Event(results.getInt("ID"), results.getString("NOME"), results.getDate("DATA"),
                                results.getTime("ORA"), Event_Type.values()[results.getInt("TIPO")],
                                Event_Location.values()[results.getInt("LUOGO")], results.getBoolean("BIGLIETTO_POLTRONA"),
                                results.getDouble("PREZZO_BPOLTRONA"), results.getBoolean("BIGLIETTO_PIEDI"),
                                results.getDouble("PREZZO_BPIEDI"), results.getInt("BIGLIETTI_VENDUTI"), results.getInt("SALE"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Cart.doGet() SQLException: " + e.getMessage());
                //throw new UnavailableException("Cart.doGet() SQLException: " + e.getMessage());
            }

            for (CartItem ci : cart) {
                if (ci.event.getSale() > 0) {
                    eventsOnSale.add("<li>" + ci.event.getName() + ": " + ci.event.getSale() +" %</li>");
                }
            }
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Carrello",
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
            out.println("<h1>Carrello</h1>");

            if (cart == null || cart.length == 0) {
                out.println("<p>Nessun biglietto nel carrello</p>");
            } else {
                out.println("<table><thead><tr><td>Nome</td><td>Data</td><td>Ora</td><td>Luogo</td><td>Ticket</td><td>Quantità</td><td></td></tr></thead><tbody>");
                for (CartItem ct: cart) {
                    out.println("<tr>");
                    out.println("<td>" + ct.event.getName() + "</td>");
                    out.println("<td>" + ct.event.getDate() + "</td>");
                    out.println("<td>" + ct.event.getTime() + "</td>");
                    out.println("<td>" + Event.EventLocationToString(ct.event.getLocation()) + "</td>");
                    out.println("<td> " + (ct.type == 1 ? "Poltrona | " : " Piedi | ") + ((ct.type == 1 ? ct.event.getPoltronaTicket() : ct.event.getPiediTicket()) * (100-ct.event.getSale()) / 100) + " €</td>");
                    out.println("<td> " + ct.qty + "</td>");
                    out.println("<td><button class=\"btn btn-danger\" onclick=\"removeFromCart(" + ct.id + ", " + ct.type + ")\">-</button></td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table>");

                out.println("<a class=\"btn btn-success\" href=\"ConfirmAndPay\">Procedi al pagamento</a>");
            }

            if (eventsOnSale.size() > 0) {
                out.println("<button type=\"button\" id=\"modalbtn\" class=\"btn btn-primary\" data-bs-toggle=\"modal\" data-bs-target=\"#modal\" hidden></button>\n" +
                        "    <div class=\"modal fade\" id=\"modal\" tabindex=\"-1\" aria-labelledby=\"modalLabel\" aria-hidden=\"true\">\n" +
                        "        <div class=\"modal-dialog\">\n" +
                        "            <div class=\"modal-content\">\n" +
                        "                <div class=\"modal-header\">\n" +
                        "                    <h5 class=\"modal-title\" id=\"modalLabel\">SCONTI PAZZI!</h5>\n" +
                        "                    <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
                        "                </div>\n" +
                        "                <div class=\"modal-body\">\n" +
                        "                    È attivo uno sconto per i biglietti degli eventi:\n" +
                        "                    <ul>");
                for (String sale : eventsOnSale) {
                    out.println(sale);
                }
                out.println("</ul>" +
                        "                </div>\n" +
                        "                <div class=\"modal-footer\">\n" +
                        "                    <button type=\"button\" class=\"btn btn-primary\" data-bs-dismiss=\"modal\">Ottimo!</button>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>");
                out.println("<script>\n" +
                        "        document.getElementById(\"modalbtn\").click();\n" +
                        "</script>");
            }
            out.println("<form id=\"remove-form\" method=\"post\" action=\"RemoveFromCart\" style=\"display: none;\">" +
                    "<input type=\"hidden\" name=\"event_id\" id=\"item-event_id\">" +
                    "<input type=\"hidden\" name=\"ticket_type\" id=\"item-ticket_type\">" +
                    "</form>");
            out.println("<script>" +
                    "function removeFromCart(eventId, ticketType) {" +
                    "   document.getElementById('item-event_id').value = eventId;" +
                    "   document.getElementById('item-ticket_type').value = ticketType;" +
                    "   document.getElementById('remove-form').submit();}" +
                    "</script>");
            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
