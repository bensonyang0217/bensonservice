package com.benson.bensonservice.utils;

import com.benson.bensonservice.config.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);
    @Value("${jwt.secretkey}")
    private String secretKey;
    @Value("${jwt.valid}")
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration validityInMs;
    
    @PostConstruct
    protected void initial() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Claims claims = Jwts.claims().setIssuedAt(new Date());

        if (principal instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            claims.setSubject(Long.toString(userPrincipal.getId()))
                    .put("roles", userPrincipal.getAuthorities());
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            claims.setSubject(oidcUser.getSubject());
            // 假設 roles 或其他屬性可以從 DefaultOidcUser 獲得
//            claims.put("roles", oidcUser.);
        }

        return generateToken(claims);
    }


    public String refreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return generateToken(claims);
    }

    private String generateToken(Claims claims) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMs.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }


    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.warn("token exception: {}", e.getMessage());
            return false;
        }
    }

}
