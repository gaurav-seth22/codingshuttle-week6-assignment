package com.codingshuttle.SecurityApp.SecurityApplication.services;


import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

   @Value("${jwt.secretKey}")
    private String jwtSecretKey;

   private SecretKey getSecretKey(){

       return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
   }
    public String generateAccessToken(User user){

       return Jwts.builder().subject(user.getId().toString())
               .claim("email",user.getEmail())
               .claim("role", user.getRoles().toString())
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + 1000*60*10))//10 mins
              // .expiration(new Date(System.currentTimeMillis() + 1000*120))
               .signWith(getSecretKey())
               .compact();

    }

    //no need to store roles in refresh token so removed it from here
    public String generateRefreshToken(User user){

        return Jwts.builder().subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
               // .expiration(new Date(System.currentTimeMillis() + 1000*120))
              //  .expiration(new Date(System.currentTimeMillis() + 1000L *60*60*24*30*6)) // 6months
                .signWith(getSecretKey())
                .compact();

    }

    public Long getuserIdFromToken(String token){

        Claims claims=Jwts.parser()
               .verifyWith(getSecretKey())
               .build()
               .parseSignedClaims(token)
               .getPayload();
        return Long.valueOf(claims.getSubject());

    }
}
