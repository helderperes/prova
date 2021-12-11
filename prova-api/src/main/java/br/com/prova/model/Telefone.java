package br.com.prova.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.prova.enums.TipoTelefoneEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "TESTE", name = "TELEFONE")
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DDD")
	private String ddd;
	
	@Column(name = "NUMERO")
	private String numero;
	
	@Column(name = "TIPOTELEFONE", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoTelefoneEnum tipo;
	
	@ManyToOne
	@JoinColumn(name = "IDCLIENTE")
	private Cliente cliente;
	
	@Transient
	private Integer key;

}