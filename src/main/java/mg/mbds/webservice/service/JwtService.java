package mg.mbds.webservice.service;

import java.security.Key;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private String SECRET = "2f2a63fc5c006da9e52dfb8fad74be43806395eda76680e06fd721a163ac65d9";
    
    private Key getKey() { 
        return Keys.hmacShaKeyFor(SECRET.getBytes()); 
    }

    public String generateToken(String email, String role) { 
        return Jwts.builder() 
                    .setSubject(email) 
                    .claim("role", role)
                    .setIssuedAt(new Date()) 
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) 
                    .signWith(getKey(), SignatureAlgorithm.HS256) 
                    .compact();
    } 

    public boolean isValid(String token) { 
        try { 
            Jwts.parserBuilder() 
                .setSigningKey(getKey()) 
                .build() 
                .parseClaimsJws(token); 
            return true; 
        } catch(Exception e) { 
            return false; 
        } 
    } 

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String extractUsername(String token) {
        return extractEmail(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && isValid(token);
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
