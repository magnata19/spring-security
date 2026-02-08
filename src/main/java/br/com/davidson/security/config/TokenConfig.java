package br.com.davidson.security.config;

import br.com.davidson.security.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

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

}
