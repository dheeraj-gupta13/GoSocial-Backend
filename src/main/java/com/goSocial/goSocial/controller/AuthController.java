package com.goSocial.goSocial.controller;


import com.goSocial.goSocial.Dto.LoginResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.goSocial.goSocial.service.AuthService;

import java.util.Map;

@Getter
@Setter
class RegisterRequest{
    String username;
    String password;
}



@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto Login(@RequestBody RegisterRequest user){
        String token = authService.verify(user.getUsername(), user.getPassword());
        return new LoginResponseDto(true, token) ;
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody RegisterRequest request){
            String username = request.getUsername();
            String password = request.getPassword();

            authService.Register(username, password);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader){
        boolean isValid = authService.validateToken(authHeader);
        if(isValid){
            return ResponseEntity.ok(
                    Map.of(
                            "validated", true,
                            "message", "Token is valid"
                    )
            );
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "validated", false,
                            "error", "Invalid token"
                    ));
        }
    }
}
