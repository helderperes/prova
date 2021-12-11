package br.com.prova.service;

import java.util.List;
import java.util.Optional;

import br.com.prova.model.Cliente;

public interface ClienteService {

	Optional<Cliente> obterPorId(Long id);

	Cliente salvar(Cliente cliente);

	Cliente atualizar(Cliente cliente);

	void deletar(Cliente cliente);

	List<Cliente> buscar(Cliente clienteFiltro);

}
