package br.com.prova.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.prova.repository.UsuarioRepository;

@Service
public class SecurityUserServiceImpl implements UserDetailsService {
	
	private static final String ADMIN = "admin";
	
	@Autowired
	private UsuarioRepository repository;

	
	@Override
	public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
		var usuario = repository.findByNome(nome).orElseThrow(() -> new UsernameNotFoundException("Usuario não cadastrado."));
		var role = "USER";
		
		if (ADMIN.equals(usuario.getNome())) {
			role = "ADMIN";
		}
		return User.builder()
				.username(usuario.getNome())
				.password(usuario.getSenha())
				.roles(role)
				.build();
	}
}
