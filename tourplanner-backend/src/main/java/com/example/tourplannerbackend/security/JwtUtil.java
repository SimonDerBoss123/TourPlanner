package com.example.tourplannerbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //geheime schlüssel zum token verifizieren und signieren. nur server kennt ihn
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //token läuft nach 24h ab dann muss user sich neu einloggen
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 Stunden

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) //wer ist eingeloggt
                .setIssuedAt(new Date()) // wann wurde Token erstellt
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) //wann läuft es ab
                .signWith(key) //mit geheimen schlüssel signieren
                .compact();  //zu String konvertieren
    }

    //liest username aus token. bei jedem request prüft server wer hat das token?
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //prüfen ob token gültig
    public boolean validateToken(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}