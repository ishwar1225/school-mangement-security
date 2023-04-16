package com.ishwar.school.management.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

   // @Value("${jwt.token.secret-key}")
    private final  String SECRET_KEY= "5A7134743777217A25432646294A404E635266556A586E3272357538782F413F";

    public JWTService()  {
    }

    public String getUserName(String token){
        return getClaim(token, Claims::getSubject);
    }
    public <T> T getClaim(String token, Function<Claims, T> resolver){
        return resolver.apply(getAllClaims(token));
    }
    public boolean isValidToken(String token, UserDetails userDetails){
        return getUserName(token).equalsIgnoreCase(userDetails.getUsername()) &&
                !isExpiredToken(token);
    }
    private boolean isExpiredToken(String token){
        return getTokenExpiration(token).before(new Date(System.currentTimeMillis()));
    }
    private  Date getTokenExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    public  String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        return Jwts.builder().setClaims(claims).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+(1000*60*60*24))).
                signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Claims getAllClaims(String token){
        return Jwts.parserBuilder().
                setSigningKey(getKey()).
                build().parseClaimsJwt(token).getBody();
    }

    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
