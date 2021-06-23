package com.example.demo.utils;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ActivationAccountRedirectView  extends RedirectView {

    public ActivationAccountRedirectView(String targetUrl){
        super(targetUrl);
    }

    @Override
    protected void sendRedirect(HttpServletRequest request,
                                HttpServletResponse response, String targetUrl,
                                boolean http10Compatible ) throws IOException {
        if (http10Compatible) {
            String session2 = RequestContextHolder.currentRequestAttributes().getSessionId();
            Cookie cookie = new Cookie("JSESSIONID",request.getSession().getId());
            response.setHeader("Location", targetUrl);
            response.addCookie(cookie);
            response.sendRedirect(targetUrl);
        }
        else {
            // Correct HTTP status code is 303, in particular for POST requests.
            response.setStatus(303);
            response.setHeader("Location", targetUrl);
        }
    }
}
