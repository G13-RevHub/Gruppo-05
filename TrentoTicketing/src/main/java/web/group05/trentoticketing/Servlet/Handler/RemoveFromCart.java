package web.group05.trentoticketing.Servlet.Handler;

import web.group05.trentoticketing.Data.CartItem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet(name = "RemoveFromCart", value = "/RemoveFromCart")
public class RemoveFromCart extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        int event_id = Integer.parseInt(request.getParameter("event_id"));
        int ticket_type = Integer.parseInt(request.getParameter("ticket_type"));


        ArrayList<CartItem> cart;
        Object cartObj = session.getAttribute("cart");
        if (cartObj != null) {
            cart = new ArrayList<>(Arrays.asList((CartItem[]) cartObj));
            CartItem item = cart.stream().filter(c -> c.id == event_id).findAny().orElse(null);
            if (item != null) {
                item.qty -= 1;
                if (item.qty <= 0) {
                    cart.remove(item);
                }
            }
        } else {
            cart = new ArrayList<>();
            cart.add(new CartItem(event_id, 1, ticket_type));
        }

        CartItem[] items = new CartItem[cart.size()];
        for (int i = 0; i < cart.size(); i++) {
            items[i] = cart.get(i);
        }
        session.setAttribute("cart", items);

        request.getRequestDispatcher("Cart").forward(request, response);
    }
}
