package cooklyst.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class DomainFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String domain = httpRequest.getServerName();

//        if (domain.equals("localhost")) {
            chain.doFilter(request, response);
//        }
    }

    @Override
    public void destroy() {
        // do nothing
    }
}