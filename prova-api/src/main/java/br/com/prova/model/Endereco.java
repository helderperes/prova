package br.com.prova.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
	

	@Column(name = "CEP")
	private String cep;
	
	@Column(name = "LOGRADOURO")
	private String logradouro;
	
	@Column(name = "BAIRRO")
	private String bairro;
	
	@Column(name = "CIDADE")
	private String localidade;
	
	@Column(name = "UF")
	private String uf;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "OUTROS")
	private String outros;
}