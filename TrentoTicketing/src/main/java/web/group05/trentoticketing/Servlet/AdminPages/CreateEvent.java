package web.group05.trentoticketing.Servlet.AdminPages;

import web.group05.trentoticketing.Data.User;
import web.group05.trentoticketing.Helper.HtmlHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CreateEvent", value = "/CreateEvent")
public class CreateEvent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // retrive data for graph

        try (PrintWriter out = response.getWriter()) {
            out.println(HtmlHelper.getHeader(session, "Crea Evento"));

            out.println("<h1>Crea Evento</h1>");
            out.println("<form action=\"CreateEventHandler\" method=post>" +
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
                    "<label for\"poltrona\">Attiva biglietti poltrona </label>" +
                    "<input type=\"checkbox\" id=\"poltrona\" name=\"poltrona\" />" +
                    "<label for\"prezzo-poltrona\">Prezzo </label>" +
                    "<input type=\"number\" min=\"0\" max=\"1000\" step=\"0.01\" id=\"prezzo-poltrona\" name=\"prezzo-poltrona\" value=\"0\" /><br><br>" +
                    "<label for\"piedi\">Attiva biglietti in piedi </label>" +
                    "<input type=\"checkbox\" id=\"piedi\" name=\"piedi\" />" +
                    "<label for\"prezzo-piedi\">Prezzo </label>" +
                    "<input type=\"number\" min=\"0\" max=\"1000\" step=\"0.01\" id=\"prezzo-piedi\" name=\"prezzo-piedi\" value=\"0\" /><br><br>" +
                    "<button type=\"submit\" class=\"btn btn-success\" >Crea</button>" +
                    "</form>");

            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
