package org.example.wallet.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.wallet.model.Nasabah;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = "zi5CoyucCE67RgPtHLHP2QmDAZ41dNAW";

    public static String generateJwtToken(Nasabah nasabah) {
        long expirationTime = 1000 * 60 * 60;

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(nasabah.id))
                .claim("id", String.valueOf(nasabah.id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Long getNasabahIdFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        String id = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class);
        return Long.valueOf(id);
    }
}

