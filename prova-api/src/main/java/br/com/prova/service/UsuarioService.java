package br.com.prova.service;

import br.com.prova.model.Usuario;

public interface UsuarioService {

	Usuario autenticar(String nome, String senha);
}
