package web.group05.trentoticketing.Servlet.AdminPages;

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

import static web.group05.trentoticketing.Helper.Help.round;

@WebServlet(name = "CreateEvent", value = "/CreateEvent")
public class CreateEvent extends HttpServlet {
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
        double[] data = new double[Event_Type.values().length];
        for (int i = 0; i < Event_Type.values().length; i++) {
            data[i] = 0;
        }
        int total = 0;
        try {
            statement = connection.createStatement();
            String query = "SELECT TIPO, SUM(BIGLIETTI_VENDUTI) FROM EVENTO GROUP BY TIPO";
            results = statement.executeQuery(query);
            while (results.next()) {
                data[results.getInt(1)] = results.getInt(2);
            }
            for (int i = 0; i < Event_Type.values().length; i++) {
                total += data[i];
            }
            for (int i = 0; i < Event_Type.values().length; i++) {
                data[i] = data[i] / total;
            }
        } catch (SQLException e) {
            System.out.println("CreateEvent.doGet() SQLException: " + e.getMessage());
            //throw new UnavailableException("CreateEvent.doGet() SQLException: " + e.getMessage());
        }

        String dataObj = "";
        for (int i = 0; i < Event_Type.values().length; i++) {
            dataObj += "{ name: '" + Event.EventTypeToString(Event_Type.values()[i]) + "', y: " + round(data[i], 2) + "},";
        }

        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Crea Evento", "", "<script src=\"https://code.highcharts.com/highcharts.js\"></script>"));

            out.println("<h1>Crea Evento</h1>");
            out.println("<div style='display: flex; flex-direction: row; gap: 30px;'>");
            out.println("<div class=\"content-hcenter\" style='width: 50%;'><form style='width: 100%;' action=\"CreateEventHandler\" method=post>" +
                    "<label for\"nome\">Nome </label>" +
                    "<input type=\"text\" id=\"nome\" name=\"nome\" placeholder=\"Nome\" required /><br><br>" +
                    "<label for\"data\">Data </label>" +
                    "<input type=\"date\" id=\"data\" name=\"data\" required /><br><br>" +
                    "<label for\"ora\">Ora </label>" +
                    "<input type=\"time\" id=\"ora\" name=\"ora\" required /><br><br>" +
                    "<label for\"tipo\">Tipo </label>" +
                    "<select id=\"tipo\" name=\"tipo\" required>" +
                    "<option value=\"0\">Concerto</option>" +
                    "<option value=\"1\">Spettacolo Teatrale</option>" +
                    "<option value=\"2\">Evento Sportivo</option>" +
                    "<option value=\"3\">Visita Guidata</option>" +
                    "<option value=\"4\">Mostre</option>" +
                    "<option value=\"5\">Museo</option>" +
                    "</select><br><br>" +
                    "<label for\"luogo\">Luogo </label>" +
                    "<select id=\"luogo\" name=\"luogo\" required>" +
                    "<option value=\"0\">Trento</option>" +
                    "<option value=\"1\">Povo</option>" +
                    "<option value=\"2\">Rovereto</option>" +
                    "<option value=\"3\">Riva del Garda</option>" +
                    "<option value=\"4\">Arco</option>" +
                    "</select><br><br>" +
                    "<label for\"poltrona\">Abilita biglietti poltrona </label>" +
                    "<input type=\"checkbox\" id=\"poltrona\" name=\"poltrona\" />" +
                    "<label for\"prezzo-poltrona\">Prezzo </label>" +
                    "<input type=\"number\" min=\"0\" max=\"1000\" step=\"0.01\" id=\"prezzo-poltrona\" name=\"prezzo-poltrona\" value=\"0\" /><br><br>" +
                    "<label for\"piedi\">Abilita biglietti in piedi </label>" +
                    "<input type=\"checkbox\" id=\"piedi\" name=\"piedi\" />" +
                    "<label for\"prezzo-piedi\">Prezzo </label>" +
                    "<input type=\"number\" min=\"0\" max=\"1000\" step=\"0.01\" id=\"prezzo-piedi\" name=\"prezzo-piedi\" value=\"0\" /><br><br>" +
                    "<button type=\"submit\" class=\"btn btn-success\" >Crea</button>" +
                    "</form></div>");
            out.println("<div style='width: 50%;'><div id=\"grafico\" style=\"width:100%; height:400px;\"></div></div></div>");

            out.println(HtmlHelper.getFooter(
                    "document.addEventListener('DOMContentLoaded', function () {\n" +
                            "            const chart = Highcharts.chart('grafico', {\n" +
                            "                chart: {\n" +
                            "                    type: 'pie'\n" +
                            "                },\n" +
                            "                title: {\n" +
                            "                    text: 'Popolarità delle Categorie'\n" +
                            "                },\n" +
                            "                tooltip: {\n" +
                            "                    valueSuffix: '%'\n" +
                            "                },\n" +
                            "                plotOptions: {\n" +
                            "                    series: {\n" +
                            "                        allowPointSelect: true,\n" +
                            "                        cursor: 'pointer',\n" +
                            "                        dataLabels: [{\n" +
                            "                            enabled: true,\n" +
                            "                            distance: 20\n" +
                            "                        },\n" +
                            "                        {\n" +
                            "                            enabled: true,\n" +
                            "                            distance: -40,\n" +
                            "                            format: '{point.percentage:.1f}%',\n" +
                            "                            style: {\n" +
                            "                                fontSize: '1.2em',\n" +
                            "                                textOutline: 'none',\n" +
                            "                                opacity: 0.7\n" +
                            "                            },\n" +
                            "                            filter: {\n" +
                            "                                operator: '>',\n" +
                            "                                property: 'percentage',\n" +
                            "                                value: 10\n" +
                            "                            }\n" +
                            "                        }]\n" +
                            "                    }\n" +
                            "                },\n" +
                            "                series: [\n" +
                            "                    {\n" +
                            "                        name: 'Percentage',\n" +
                            "                        colorByPoint: true,\n" +
                            "                        data: [" + dataObj + "]\n" +
                            "                    }\n" +
                            "                ]\n" +
                            "            });\n" +
                            "        });"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
