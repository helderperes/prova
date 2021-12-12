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
	
	@Autowired
	private UsuarioRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
		var usuario = repository.findByNome(nome).orElseThrow(() -> new UsernameNotFoundException("Usuario não cadastrado."));
		return User.builder().username(usuario.getNome()).password(usuario.getSenha()).roles(usuario.getPermissao()).build();
	}
}
