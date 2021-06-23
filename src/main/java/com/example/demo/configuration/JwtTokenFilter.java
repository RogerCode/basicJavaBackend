package com.example.demo.configuration;

import com.example.demo.utils.JwtTokenUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.isEmpty;

public class JwtTokenFilter implements Filter {

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Override
    public void doFilter(final ServletRequest httpServletRequest,
                         final ServletResponse httpServletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) httpServletRequest;
        final HttpServletResponse response = (HttpServletResponse) httpServletResponse;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || header.equals("Bearer undefined")){
            header = request.getParameter("token");
        }
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            throw new MalformedJwtException("jwt token not valid");
        }
        String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            SecurityContextHolder.clearContext();
            throw new MalformedJwtException("jwt token not valid");
        }
        if (checkUserNameAndPassword(token)) {
            SecurityContextHolder.clearContext();
            throw new MalformedJwtException("jwt token not valid");
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkUserNameAndPassword (String token){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userdetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        return jwtTokenUtil.getUsername(token) == userdetails.getUsername();
    }

}
