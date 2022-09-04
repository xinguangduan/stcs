package org.stcs.server.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stcs.server.config.Configuration;


@WebFilter(urlPatterns = "/*", filterName = "GlobalFilter")
@Slf4j
@Component
public class GlobalFilter implements Filter {

    @Autowired
    private Configuration config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("GlobalFilter init");
    }

    @Override
    public void destroy() {
        log.info("Filter destroyed");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServlet = (HttpServletRequest) request;
        final String reqUrl = httpServlet.getRequestURI();
        if (reqUrl.startsWith("/health") || reqUrl.startsWith("/actuator")) {
            log.debug("non business request, ignore it.");
        }
        chain.doFilter(request, response);
    }

}
