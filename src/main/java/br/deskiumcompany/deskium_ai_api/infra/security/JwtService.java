package br.deskiumcompany.deskium_ai_api.infra.security;

import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//Geração de tokens JWT Validos para o login
@Service
public class JwtService {

    private String secret = "unfKnk4NwsCldvt8Io6aO0QS6fMdFkwogtlbUgZjQsFCOazryYP5vltTAQIfn4qHjyDnxzbti4R6kFACN9MBEvVaX4YD96o7tecfsq1d4SPh39dMCQcEbv22jLOKJ2H439pbsXxHWHo3PaMu0jG4QiFGZM8g7LPNFxld5IAyxzM0rCmyZSsSQYTQ7EzMTNAiMx5h1KVXOBHQZStyX4E8KRzziFuR2DnUJi2O2cMfGUy4OnzdAPz5NMIq5GDQUpkt";

    //gera a chave de assinatura do JWT.
    public String generateToken(Usuario usuario){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer("deskium-api")
                .withSubject(usuario.getEmail())
                .withExpiresAt(genExpirationDate())
                .sign(algorithm);

        return token;
    }

    public String validateToken(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("deskium-api")
                .build()
                .verify(token)
                .getSubject();

    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
