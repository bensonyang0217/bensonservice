package com.benson.bensonservice.utils;

import com.benson.bensonservice.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void initial() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getAuthorities());
//        claims.put("nickname", securityUserDetails.getNickname());
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

    public Authentication getAuthentication(String token) {
        User userDetails = (User) userDetailsService.loadUserByUsername(getUsername(token));
//        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
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
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new InvalidJwtAuthenticationException("Invalid JWT token");
//        }
        }
    }
}
