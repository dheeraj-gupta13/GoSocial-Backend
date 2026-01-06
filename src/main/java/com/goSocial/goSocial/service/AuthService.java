package com.goSocial.goSocial.service;

import com.goSocial.goSocial.Exception.UserAlreadyExistsException;
import com.goSocial.goSocial.Repository.AuthRepository;
import com.goSocial.goSocial.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);


    public String verify(String username, String password){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if(authentication.isAuthenticated()){
            int user_id = authRepository.getUserId(username);
            System.out.println("VERIFY : " + user_id);
            return jwtService.generateToken(user_id);
        }

        return "fail";
    }


    public void Register(String username , String password){

        if(authRepository.existsByUsername(username)){
            throw new UserAlreadyExistsException("Username already taken");
        }

        String hashedPassword = encoder.encode(password);
        authRepository.register(username, hashedPassword);
    }

    public boolean validateToken(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return false;
        }
        String token = authHeader.substring(7);

        return jwtService.validateToken(token);
    }
}
