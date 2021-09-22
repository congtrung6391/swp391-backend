package com.swp391.onlinetutorapplication.onlinetutorapplication.utils.jwtUtils;

import com.swp391.onlinetutorapplication.onlinetutorapplication.model.userDetails.UserDetailsImplement;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTUtils {

    @Value("${tutor-online.app.jwtSecret}")
    private String SECRETE_TOKEN;

    @Value("${tutor-online.app.jwtExpirationMs}")
    private int EXPIRATION_TIME;


    public String generateJwtToken(UserDetailsImplement userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }
    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS512, SECRETE_TOKEN)
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(SECRETE_TOKEN).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRETE_TOKEN).parseClaimsJws(token);
            return true;
        }catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT refreshToken: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT refreshToken is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT refreshToken is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Bean
    public InMemoryTokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
