package br.com.davidson.security.config;

import br.com.davidson.security.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    @Value("${token.secret}")
    private String secret;

    public String generateToken(User user){
        return JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(900)) // Token expira em 15 minutos
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(secret));
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            DecodedJWT decode = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            return Optional.of(JWTUserData.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .email(decode.getSubject()).build());

        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
