package br.com.prova.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.prova.dto.ClienteDTO;
import br.com.prova.exception.NegocioException;
import br.com.prova.model.Cliente;
import br.com.prova.service.ClienteService;

@RestController
@RequestMapping("/api/cliente")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClienteController {

	@Autowired
	private ClienteService service;
	

	@GetMapping
	public ResponseEntity<?> buscar(
			@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "cpf", required = false) String cpf) {

		var filtro = new Cliente();
		filtro.setNome(nome);
		filtro.setCpfCnpj(cpf);
		var clientes = service.buscar(filtro);
		
		List<ClienteDTO> retorno = 
				clientes.stream().map(this::toClienteDTO).collect(Collectors.toList());
		return ResponseEntity.ok(retorno);
	}

	@GetMapping("{id}")
	public ResponseEntity<?> obterCliente(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(c -> 
			new ResponseEntity(toClienteDTO(c), HttpStatus.OK)).orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody ClienteDTO dto) {
		try {
			var entidade = toCliente(dto);
			prepararEmails(dto, entidade);
			prepararTelefones(dto, entidade);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (NegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody ClienteDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				var cliente = toCliente(dto);
				
				cliente.setId(entity.getId());
				prepararEmails(dto, cliente);
				prepararTelefones(dto, cliente);
				
				service.atualizar(cliente);
				return ResponseEntity.ok(cliente);
			} catch (NegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Cliente não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			   service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Cliente não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	private void prepararTelefones(ClienteDTO dto, Cliente entidade) {
		dto.getTelefones().forEach(tel -> tel.setCliente(entidade));
	}

	private void prepararEmails(ClienteDTO dto, Cliente entidade) {
		dto.getEmails().forEach(e -> e.setCliente(entidade));
	}
	
	private Cliente toCliente(ClienteDTO dto) {
		return Cliente
				.builder()
					.cpfCnpj(dto.getCpfCnpj())
					.nome(dto.getNome())
					.endereco(dto.getEndereco())
					.telefones(dto.getTelefones())
					.emails(dto.getEmails())
				.build();
	}
	
	private ClienteDTO toClienteDTO(Cliente cliente) {
		return ClienteDTO
				.builder()
				.id(cliente.getId())
				.nome(cliente.getNome())
				.cpfCnpj(cliente.getCpfCnpj())
				.endereco(cliente.getEndereco())
				.telefones(cliente.getTelefones())
				.emails(cliente.getEmails())
				.build();
	}
	
}
