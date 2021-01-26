package com.mobile.app.ws.utils;

import com.mobile.app.ws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Random;

public class Util {

    public static String generateRandomUserId() {
        return getRandomIdString();
    }

    public static String generateRandomAddressId() {
        return getRandomIdString();
    }

    private static String getRandomIdString() {
        Random rand = new Random();

        int rand_int1 = rand.nextInt(1000);
        int rand_int2 = rand.nextInt(1000);

        String id = String.valueOf(String.valueOf(rand_int1) + String.valueOf(rand_int2));
        return id;
    }

    public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET)
                .parseClaimsJws(token).getBody();
        Date expirationDate = claims.getExpiration();
        Date today = new Date();
        return expirationDate.before(today);
    }

    public static String generateEmailVerificationToken(String userId) {
        String token = Jwts.builder().setSubject(userId).setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET).compact();
        return token;
    }
}
