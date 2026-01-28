package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import util.JwtUtil;

import java.io.IOException;


public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("TOKEN_MISSING");
            return;
        }
   int Seventh_Index=7;
        String token = authHeader.substring(Seventh_Index);

        try {
            String username = JwtUtil.validateTokenAndGetUsername(token);
            req.setAttribute("username", username);
            chain.doFilter(request, response);
        } catch (Exception e) {
     		resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("INVALID_OR_EXPIRED_TOKEN");
        }
    }
}
