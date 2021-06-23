package com.example.demo.service;

import com.example.demo.entity.UserRepository;
import com.example.demo.entity.VerificationToken;
import com.example.demo.events.OnRegistrationCompleteEvent;
import com.example.demo.pojo.common.ErrorResponse;
import com.example.demo.utils.ActivationAccountRedirectView;
import com.example.demo.utils.JwtTokenUtil;
import com.example.demo.entity.User;
import com.example.demo.pojo.authentication.LoginRequest;
import com.example.demo.pojo.authentication.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    public ResponseEntity login(LoginRequest loginRequest){
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            User user = (User) authenticate.getPrincipal();
            return ResponseEntity.ok()
                    .body(jwtTokenUtil.generateAccessToken(user));

        } catch (BadCredentialsException ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(ErrorResponse.Error._1_1);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    public RedirectView registerConfirm(String token, HttpServletRequest request, HttpServletResponse response){
        VerificationToken verificationToken = userRepository.getVerificationToken(token);
        if (verificationToken == null) {
            return new ActivationAccountRedirectView("http://localhost:3000/bad-user");
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ActivationAccountRedirectView("http://localhost:3000/bad-user");
        }
        user.setEnabled(true);
        userRepository.save(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RedirectView redirectView = new RedirectView("http://localhost:3000/succesful-register?token="+ jwtTokenUtil.generateAccessToken(user));

        return redirectView;
    }

    public ResponseEntity register(RegisterRequest registerRequest, HttpServletRequest request){
        if(userRepository.existsById(registerRequest.getUserName())){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        userRepository.save(user);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                request.getLocale(), appUrl));
        return ResponseEntity.ok("All okey");
    }
}
