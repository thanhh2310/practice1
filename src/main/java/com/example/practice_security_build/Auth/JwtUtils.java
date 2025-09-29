package com.example.practice_security_build.Auth;

import com.example.practice_security_build.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String privateKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(privateKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername (String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public Date extractIssuedAt (String token){
        Claims claims = extractAllClaims(token);
        return claims.getIssuedAt();
    }

    public Date extractExpiration (String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public List<String> extractRoles (String token){
        Object roleObj = extractAllClaims(token).get("roles");
        if(roleObj instanceof List<?>){
            return ((List<?>) roleObj ).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, User user){
        final String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

}
