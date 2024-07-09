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
            out.println(HtmlHelper.getHeader(user, "Lista Eventi"));

            out.println("<h1>Eventi + " + Event_Type.values()[event_type] + "</h1>");



            out.println(HtmlHelper.getFooter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
