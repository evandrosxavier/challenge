package br.com.fiap.challenge.security;

import br.com.fiap.challenge.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "minha-chave-super-secreta-com-mais-de-32-bytes";

    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String gerarToken(Usuario usuario) {

        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + 1000 * 60 * 60); // 1h

        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .claim("id", usuario.getId())
                .claim("tipo", usuario.getTipoUsuario().name())
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

}