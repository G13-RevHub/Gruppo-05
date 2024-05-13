package web.group05.trentoticketing;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //chain.doFilter(request, response);

        HttpServletRequest hreq = (HttpServletRequest) request;

        String username = hreq.getParameter("username");
        String password = hreq.getParameter("password");
        if (username.equals("admin") && password.equals("admin")) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("index.html").include(request, response);
        }
    }
}
