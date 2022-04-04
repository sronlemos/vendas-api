package com.github.sronLemos.vendas.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.sronLemos.vendas.domain.entity.Usuario;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario usuario) {
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Date data = Date.from(dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant());

        Map<String, Object> claims = new HashMap<>();
        claims.put("emaildousuario", "usuario@gmail.com");
        claims.put("usuario", usuario.getLogin());
        claims.put("roles", "admin");
        claims.put("expiration", dataHoraExpiracao.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .setExpiration(data)
                .setClaims(claims)
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenValido(String token) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Claims claims = obterClaims(token);
            String expiration = (String) claims.get("expiration");
            LocalDateTime dataToken = LocalDateTime.parse(expiration, formatter);
            return !LocalDateTime.now().isAfter(dataToken);

        } catch (Exception e) {
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException {
        return (String) obterClaims(token).get("usuario");
    }
}
