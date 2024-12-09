package transaction.prixBanque.transaction.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de la validation du token : " + e.getMessage());
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getKey()) // Use the same key as in token generation
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Long.class);
    }

//    public Long extractUserId(String token) {
//        Claims claims = extractAllClaims(token);
//        Object idClaim = claims.get("id");
//        if (idClaim instanceof Integer) {
//            return ((Integer) idClaim).longValue();
//        } else if (idClaim instanceof Long) {
//            return (Long) idClaim;
//        } else {
//            throw new IllegalArgumentException("Invalid ID type in token claims");
//        }
//    }
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
