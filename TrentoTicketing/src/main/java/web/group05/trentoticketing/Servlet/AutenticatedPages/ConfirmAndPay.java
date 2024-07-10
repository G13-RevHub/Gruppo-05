package web.group05.trentoticketing.Servlet.AutenticatedPages;

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

@WebServlet(name = "ConfirmAndPay", value = "/ConfirmAndPay")
public class ConfirmAndPay extends HttpServlet {
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
        User user = (User)session.getAttribute("user");
        CartItem[] cart = (CartItem[])session.getAttribute("cart");

        if (cart == null) {
            System.out.println("cart non presente in sessione");
            request.getRequestDispatcher("Home").forward(request, response);
            return;
        }
        ArrayList<int[]> freeTickets = new ArrayList<int[]>();

        Statement statement = null;
        ResultSet results = null;
        try {
            statement = connection.createStatement();

            int i = 0;
            for (CartItem item: cart) {
                int acquistati = 0;
                for (int j = 0; j < item.qty; j++) {
                    if (user.getBiglietti_aquistati() > 0 && (user.getBiglietti_aquistati() + j) % 5 == 0) {
                        acquistati++;
                    }
                }
                if (acquistati > 0) {
                    freeTickets.add(new int[] { i, acquistati });
                }
                user.acqista_biglietti(item.qty);

                String query = "SELECT * FROM EVENTO WHERE ID = " + item.id;
                results = statement.executeQuery(query);
                while (results.next()) {
                    item.event = new Event(results.getInt("ID"), results.getString("NOME"), results.getDate("DATA"),
                            results.getTime("ORA"), Event_Type.values()[results.getInt("TIPO")],
                            Event_Location.values()[results.getInt("LUOGO")], results.getBoolean("BIGLIETTO_POLTRONA"),
                            results.getDouble("PREZZO_BPOLTRONA"), results.getBoolean("BIGLIETTO_PIEDI"),
                            results.getDouble("PREZZO_BPIEDI"), results.getInt("BIGLIETTI_VENDUTI"), results.getInt("SALE"));
                }

                query = "UPDATE EVENTO SET BIGLIETTI_VENDUTI = " + (item.event.getTickets_sold() + item.qty) + " WHERE ID = " + item.event.getId();
                statement.executeUpdate(query);
                query = "UPDATE UTENTE SET BIGLIETTI_ACQUISTATI = " + user.getBiglietti_aquistati() + " WHERE USERNAME = '" + user.getUsername() + "'";
                statement.executeUpdate(query);
                i++;
            }
        } catch (SQLException e) {
            System.out.println("ConfirmAndPay.doGet() SQLException: " + e.getMessage());
            //throw new UnavailableException("ConfirmAndPay.doGet() SQLException: " + e.getMessage());
        }

        session.setAttribute("cart", new CartItem[] {});
        session = request.getSession(false);

        double totale = 0, regalo = 0;
        for (CartItem item : cart) {
            totale += (item.type == 1 ? item.event.getPoltronaTicket() : item.event.getPiediTicket()) * (100-item.event.getSale()) / 100 * item.qty;
        }
        for (int[] gift : freeTickets) {
            regalo += (cart[gift[0]].type == 1 ? cart[gift[0]].event.getPoltronaTicket() : cart[gift[0]].event.getPiediTicket()) * (100-cart[gift[0]].event.getSale()) / 100 * gift[1];
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Conferma Pagamento", "p { text-align: center; }"));

            out.println("<h2>Pagamento avvenuto con successo!</h2>");
            out.println("<p>Totale pagato: " + (totale - regalo) + " €</p>");
            out.println("<p>Torna alla <a href=\"Home\">Home</a></p>");

            if (freeTickets.size() > 0) {
                out.println("<button type=\"button\" id=\"modalbtn\" class=\"btn btn-primary\" data-bs-toggle=\"modal\" data-bs-target=\"#modal\" hidden></button>\n" +
                        "    <div class=\"modal fade\" id=\"modal\" tabindex=\"-1\" aria-labelledby=\"modalLabel\" aria-hidden=\"true\">\n" +
                        "        <div class=\"modal-dialog\">\n" +
                        "            <div class=\"modal-content\">\n" +
                        "                <div class=\"modal-header\">\n" +
                        "                    <h5 class=\"modal-title\" id=\"modalLabel\">CONGRATULAZIONI!</h5>\n" +
                        "                    <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
                        "                </div>\n" +
                        "                <div class=\"modal-body\">\n" +
                        "                    Grazie all'offerta 5 al prezzo di 4 non ti sono stati addebitati costi per i seguenti biglietti:\n" +
                        "                    <ul>");
                for (int[] gift : freeTickets) {
                    out.println("<li> + " + cart[gift[0]].event.getName() + " x" + gift[1] + "</li>");
                }
                out.println("</ul>" +
                        "Hai risparmiato " + regalo + " €!" +
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

            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
