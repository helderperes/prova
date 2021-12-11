package br.com.prova.dto;

import java.util.Set;

import br.com.prova.model.Email;
import br.com.prova.model.Endereco;
import br.com.prova.model.Telefone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

	private Long id;
	private String nome;
	private String cpfCnpj;
	private Endereco endereco;
	private Set<Telefone> telefones;
	private Set<Email> emails;
}
