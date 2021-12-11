package br.com.prova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.prova.dto.EnderecoDTO;
import br.com.prova.service.EnderecoService;

@RestController
@RequestMapping("/api/cep/consulta")
public class EnderecoController {
	
	@Autowired
	private EnderecoService service;
	
	
	@GetMapping("/{cep}")
	public ResponseEntity<EnderecoDTO> consultarCep(@PathVariable("cep") String cep) {
		return service.consultar(cep)
				.map(dto -> new ResponseEntity<EnderecoDTO>(dto, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<EnderecoDTO>(HttpStatus.NOT_FOUND));
	}
}
