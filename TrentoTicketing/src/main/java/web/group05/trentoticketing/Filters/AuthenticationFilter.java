package web.group05.trentoticketing.Filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AuthenticationFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpSession session = hreq.getSession(true);

        if (session != null && session.getAttribute("user") != null) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("login.html").forward(request, response);
        }
    }
}
