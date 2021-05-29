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
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
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
        chain.doFilter(request, response);
    }

    private boolean checkUserNameAndPassword (String token){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userdetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        return jwtTokenUtil.getUsername(token) == userdetails.getUsername();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        if(request.getRequestURI().contains("login") || request.getRequestURI().contains("register")){
            return true;
        }
        return false;
    }
}
