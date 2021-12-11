package br.com.prova.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.prova.service.JwtService;

public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private UserDetailsService service;

	public JwtTokenFilter(JwtService jwtService, UserDetailsService service) {
		this.jwtService = jwtService;
		this.service = service;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var authorization = request.getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Bearer")) {

			var token = authorization.split(" ")[1];
			var isTokenValid = jwtService.isTokenValido(token);

			if (isTokenValid) {
				var login = jwtService.obterLoginUsuario(token);
				var usuarioAutenticado = service.loadUserByUsername(login);

				var user = new UsernamePasswordAuthenticationToken(usuarioAutenticado, null,
						usuarioAutenticado.getAuthorities());

				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(user);

			}
		}

		filterChain.doFilter(request, response);
	}

}
