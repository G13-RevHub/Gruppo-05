package web.group05.trentoticketing.Filters;

import web.group05.trentoticketing.Data.User;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AdminFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpSession session = hreq.getSession(false);

        if (((User)session.getAttribute("user")).getIs_admin()) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("account/login.html").forward(request, response);
        }
    }
}
