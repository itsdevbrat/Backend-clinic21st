package com.clinic.website.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final byte[] SIGNING_KEY = "clinic-key".getBytes();
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 24*60*60;

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String subject) {

        Claims claims = Jwts.claims().setSubject(subject);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://www.clinic21st.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

//    public Boolean validateToken(String token, ) {
//        final String username = getEmailFromToken(token);
//        return (
//                username.equals(userDetails.getUsername())
//                        && !isTokenExpired(token));
//    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
