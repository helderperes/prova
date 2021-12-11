package br.com.prova.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.prova.model.Usuario;
import br.com.prova.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {

	@Value("${jwt.expiracao}")
	private String expiracao;

	@Value("${jwt.chave-assinatura}")
	private String chaveAssinatura;

	@Override
	public String gerarToken(Usuario usuario) {
		var exp = Long.valueOf(expiracao);
		var dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
		var instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		java.util.Date data = Date.from(instant);

		var horaExpiracaoToken = dataHoraExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

		var token = Jwts.builder()
							.setExpiration(data)
							.setSubject(usuario.getNome())
							.claim("userid", usuario.getCodigo())
							.claim("nome", usuario.getNome())
							.claim("horaExpiracao", horaExpiracaoToken)
							.signWith(SignatureAlgorithm.HS512, chaveAssinatura).compact();

		return token;
	}

	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(chaveAssinatura).parseClaimsJws(token).getBody();
	}

	@Override
	public boolean isTokenValido(String token) {
		try {
			var claims = obterClaims(token);
			java.util.Date dataEx = claims.getExpiration();
			var dataExpiracao = dataEx.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			var dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
			return !dataHoraAtualIsAfterDataExpiracao;
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	@Override
	public String obterLoginUsuario(String token) {
		var claims = obterClaims(token);
		return claims.getSubject();
	}

}
