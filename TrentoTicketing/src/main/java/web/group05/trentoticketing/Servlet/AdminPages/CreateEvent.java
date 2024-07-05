package web.group05.trentoticketing.Servlet.AdminPages;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CreateEvent", value = "/CreateEvent")
public class CreateEvent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // retrive data for graph

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head><title>Crea Evento</title></head><body>");
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
                    "<label for\"prezzo\">Prezzo </label>" +
                    "<input type=\"number\" min=\"0\" max=\"1000\" step=\"0.01\" id=\"prezzo\" name=\"prezzo\" required /><br><br>" +
                    "<label for\"tipo_biglietto\">Tipo Biglietto </label>" +
                    "<select id=\"tipo_biglietto\" name=\"tipo_biglietto\" required>" +
                    "<option value=\"0\">Poltrona</option>" +
                    "<option value=\"1\">In Piedi</option>" +
                    "</select><br><br>" +
                    "<button type=\"submit\">Crea</button>" +
                    "</form>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
