package com.attendease.filters;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) {}

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                throws IOException, ServletException {

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
            response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, Access-Control-Allow-Credentials");
            response.setHeader("Access-Control-Max-Age", "3600");

            // ✅ Log to console to confirm filter execution
            System.out.println("🟢 CorsFilter triggered for " + request.getMethod() + " " + request.getRequestURI());

            // If preflight request, return early
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            chain.doFilter(req, res);
        }

        @Override
        public void destroy() {}
}


