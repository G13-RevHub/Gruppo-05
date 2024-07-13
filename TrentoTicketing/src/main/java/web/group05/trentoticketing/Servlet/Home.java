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

@WebServlet(name = "Home", value = "/Home")
public class Home extends HttpServlet {
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

        Statement statement = null;
        ResultSet results = null;
        ArrayList<Event> events = new ArrayList<>();
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM EVENTO ORDER BY BIGLIETTI_VENDUTI DESC";
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
            out.println(HtmlHelper.getHeader(session, "Home",
                    ".main-div { display: flex; flex-direction: row; }" +
                            ".div1 { width: 400px; padding: 10px; display: flex; flex-direction: column; align-items: center; }" +
                            ".div2 { width: 100%; padding: 10px; display: flex; flex-direction: column; align-items: center; }" +
                            ".success-table { width: 100%; border-collapse: collapse; }" +
                            ".success-table thead { font-weight: bold; }" +
                            ".success-table thead tr td, .success-table tbody tr td { padding: 6px; text-align: center; border: 1px solid #ddd; }"));

            out.println("<h1>TrentoTicketing</h1><br/>\n" +
                    "<div class='main-div'>" +
                    "<div class='div1'>" +
                    "        <img src=\"images/logo.png\" />\n" +
                    "        <br><br>\n" +
                    "\n" +
                    "        <div style=\"bottom: 0px; margin-top: 5px;\">\n" +
                    "            <table>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">Sede</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">Trento, via Roma 11</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">P. IVA</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">79846512345</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">Mail</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">info@trentoticketing.it</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td style=\"padding: 2px; text-align: right; font-weight: bold\">Telefono</td>\n" +
                    "                    <td style=\"padding: 2px; text-align: left;\">389 154 3148</td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </div>" +
                    "</div>" +
                    "<div class='div2'>");

            if (events.size() > 0) {
                out.println("<h6>Gli eventi di maggior successo</h6>");
                out.println("<table class='success-table'><thead><tr><td>Nome</td><td>Tipo</td><td>Data</td><td>Ora</td><td>Luogo</td><td></td></tr></thead><tbody>");
                for (int i = 0; i < 3 && i < events.size(); i++) {
                    Event e = events.get(i);
                    out.println("<tr>");
                    out.println("<td>" + e.getName() + "</td>");
                    out.println("<td>" + Event.EventTypeToString(e.getType()) + "</td>");
                    out.println("<td>" + e.getDate() + "</td>");
                    out.println("<td>" + e.getTime() + "</td>");
                    out.println("<td>" + Event.EventLocationToString(e.getLocation()) + "</td>");
                    out.println("<td><a class='btn btn-secondary' href='EventPage?id=" + e.getId() + "'>Apri</a></td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table><br/><br/>");
            }

            out.println("<h6>Gli sconti del momento</h6>");
            out.println("<div id=\"saleTable\" style='width: 100%;'></div>");

            out.println(HtmlHelper.getFooter(
                    "function eventLocationToString(el) {\n" +
                            "            switch (el) {\n" +
                            "                case 'TRENTO': return 'Trento';\n" +
                            "                case 'POVO': return 'Povo';\n" +
                            "                case 'ARCO': return 'Arco';\n" +
                            "                case 'ROVERETO': return 'Rovereto';\n" +
                            "                case 'RIVA_DEL_GARDA': return 'Riva del Garda';\n" +
                            "                default: return null;\n" +
                            "            }\n" +
                            "        }\n" +
                            "        function eventTypeToString(et) {\n" +
                            "            switch (et) {\n" +
                            "                case 'MUSEO': return 'Museo';\n" +
                            "                case 'MOSTRE': return 'Mostre';\n" +
                            "                case 'CONCERTO': return 'Concerto';\n" +
                            "                case 'VISITA_GUIDATA': return 'Visita Guidata';\n" +
                            "                case 'EVENTO_SPORTIVO': return 'Evento Sportivo';\n" +
                            "                case 'SPETTACOLO_TEATRALE': return 'Spettacolo Teatrale';\n" +
                            "                default: return null;\n" +
                            "            }\n" +
                            "        }\n" +
                            "        function getSaleEvents() {\n" +
                            "            var my_JSON_object;\n" +
                            "            var url = \"http://localhost:8080/TrentoTicketing_war_exploded/EventOnSale\";\n" +
                            "            var xhttp = new XMLHttpRequest();\n" +
                            "            xhttp.open(\"GET\", url, true);\n" +
                            "            xhttp.responseType = \"json\";\n" +
                            "            xhttp.onreadystatechange = function () {\n" +
                            "                var done = 4, ok = 200;\n" +
                            "                if (this.readyState === done && this.status === ok) {\n" +
                            "                    events = this.response;\n" +
                            "                    var saleTableBody = \"\";\n" +
                            "                    if (events.length > 0) {\n" +
                            "                        saleTableBody = \"<table class='success-table'><thead><tr><td>Nome</td><td>Tipo</td><td>Data</td><td>Ora</td><td>Luogo</td><td>Sconto</td><td></td></tr></thead><tbody id='sale-table-content'>\";\n" +
                            "                        for (let i = 0; i < events.length && i < 3; i++) {\n" +
                            "                            saleTableBody += \"<tr>\";\n" +
                            "                            saleTableBody += \"<td>\" + events[i].name + \"</td>\";\n" +
                            "                            saleTableBody += \"<td>\" + eventTypeToString(events[i].type) + \"</td>\";\n" +
                            "                            saleTableBody += \"<td>\" + events[i].date + \"</td>\";\n" +
                            "                            saleTableBody += \"<td>\" + events[i].time + \"</td>\";\n" +
                            "                            saleTableBody += \"<td>\" + eventLocationToString(events[i].location) + \"</td>\";\n" +
                            "                            saleTableBody += \"<td>\" + events[i].sale + \"</td>\";\n" +
                            "                            saleTableBody += \"<td><a class='btn btn-secondary' href='EventPage?id=\" + events[i].id + \"'>Apri</a></td>\";\n" +
                            "                            saleTableBody += \"</tr>\";\n" +
                            "                        }\n" +
                            "                        saleTableBody += \"</tbody></table>\";\n" +
                            "                    } else {\n" +
                            "                        saleTableBody = \"<p>Non ci sono eventi in sconto al momento</p>\"\n" +
                            "                    }\n" +
                            "                    document.getElementById(\"saleTable\").innerHTML = saleTableBody;\n" +
                            "                }\n" +
                            "            };\n" +
                            "            xhttp.send();\n" +
                            "        }\n" +
                            "        getSaleEvents();\n" +
                            "        setInterval(getSaleEvents, 15000);"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
