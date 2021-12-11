package br.com.prova.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.prova.exception.NegocioException;
import br.com.prova.model.Usuario;
import br.com.prova.repository.UsuarioRepository;
import br.com.prova.service.UsuarioService;
import br.com.prova.utils.Constantes;

@Service
public class UsuarioServiceImpl implements UsuarioService {


	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Usuario autenticar(String nome, String senha) {
		
		var usuario = repository.findByNome(nome)
				.orElseThrow(() -> new NegocioException(Constantes.USR_NAO_ENCONTRADO));

		var senhaValida = encoder.matches(senha, usuario.getSenha());

		if (!senhaValida) {
			throw new NegocioException(Constantes.SENHA_INVALIDA);
		}

		return usuario;
	}
}
