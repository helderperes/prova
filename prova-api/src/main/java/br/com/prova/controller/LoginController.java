package br.com.prova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.prova.dto.TokenDTO;
import br.com.prova.dto.UsuarioDTO;
import br.com.prova.exception.NegocioException;
import br.com.prova.service.JwtService;
import br.com.prova.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private JwtService jwtService;

	
	@PostMapping("/usuario/autenticar")
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO dto) {
		try {
			var usr = service.autenticar(dto.getNome(), dto.getSenha());
			var token = jwtService.gerarToken(usr);
			var tokenDTO = new TokenDTO(usr.getNome(), token);
			return ResponseEntity.ok(tokenDTO);
		} catch (NegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
