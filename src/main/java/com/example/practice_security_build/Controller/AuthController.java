package com.example.practice_security_build.Controller;

import com.example.practice_security_build.Auth.JwtUtils;
import com.example.practice_security_build.DTO.LoginRequest;
import com.example.practice_security_build.Model.User;
import com.example.practice_security_build.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            User user = (User) userService.loadUserByUsername(request.getUsername());
            String token = jwtUtils.generateToken(user);
            Map<String, Object> body = Map.of(
                    "token Types", "Bearer",
                    "access ToKen", token,
                    "expiresIn", jwtUtils.extractExpiration(token).getTime()
            );
            return ResponseEntity.ok(body);
        }catch (BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error","Invalid "));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) throws Exception {
        userService.registerUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Create successfully!");
    }
}
