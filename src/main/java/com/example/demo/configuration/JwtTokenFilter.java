package com.example.demo.configuration;

import com.example.demo.utils.JwtTokenUtil;
import com.example.demo.entity.UserRepository;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.springframework.util.StringUtils.isEmpty;


public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepo;

    @Override
    public void doFilter(final ServletRequest httpServletRequest,
                         final ServletResponse httpServletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) httpServletRequest;
        final HttpServletResponse response = (HttpServletResponse) httpServletResponse;

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null){
            header = request.getParameter("token");
        }
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            throw new MalformedJwtException("jwt token not valid");
        }
        final String token = header.split(" ")[1].trim();
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
        SecurityContextHolder.getContext().getAuthentication();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userdetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        return jwtTokenUtil.getUsername(token) == userdetails.getUsername();
    }

}
