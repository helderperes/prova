package br.com.prova.service;

import java.util.Optional;

import br.com.prova.dto.EnderecoDTO;

public interface EnderecoService {

	Optional<EnderecoDTO> consultar(String cep);

}
