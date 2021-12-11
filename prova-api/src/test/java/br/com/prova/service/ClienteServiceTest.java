package br.com.prova.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.prova.enums.TipoTelefoneEnum;
import br.com.prova.exception.NegocioException;
import br.com.prova.model.Cliente;
import br.com.prova.model.Email;
import br.com.prova.model.Endereco;
import br.com.prova.model.Telefone;
import br.com.prova.repository.ClienteRepository;
import br.com.prova.utils.Constantes;

@SpringBootTest
class ClienteServiceTest {
	
	@MockBean
	private ClienteRepository repository;
	
	@Autowired
	private ClienteService service;
	
	private Cliente cliente;
	
	
	@BeforeEach
	public void setUp() {
		cliente = Cliente
				.builder()
					.cpfCnpj("000.000.000-00")
					.nome("Teste")
					.endereco(endereco())
					.telefones(getTelefones())
					.emails(getEmails())
				.build();
		when(repository.save(cliente)).thenReturn(cliente);
	}
	
	
	@Test
	void removerMascaraCpfCnpj() {
		var c = service.salvar(cliente);
		assertEquals("00000000000", c.getCpfCnpj());
		
		verify(repository).save(cliente);
	}
	
	@Test
	void removerMascaraCep() {
		var c = service.salvar(cliente);
		when(repository.save(cliente)).thenReturn(cliente);
		assertEquals("00000000", c.getEndereco().getCep());
		
		verify(repository).save(cliente);
	}
	
	@Test
	void removerMascaraNumTel() {
		var c = service.salvar(cliente);
		when(repository.save(cliente)).thenReturn(cliente);
		c.getTelefones().forEach(t -> assertEquals("999999999", t.getNumero()));
		
		verify(repository).save(cliente);
	}

	@Test
	void regraCamposCliente() {
		cliente.setCpfCnpj(null);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_CPF_CNPJ, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setNome(null);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_NOME, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setNome("Te");
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.NOME_MIN_3_CARACT, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setNome("Testeeeeeeeeeeeeeeeeeeeeeeeee Testeeeeeeeeeeeeeeeeeeeeeeeee Testeeeeeeeeeeeeeeeeeeeeeeeee Testeeeeeee");
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.NOME_MAX_100_CARACT, NegocioException.class, () -> service.salvar(cliente));
		
		verifyNoInteractions(repository);
	}

	@Test
	void regraCamposEndereco() {
				
		cliente.setEndereco(null);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_ENDERECO, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEndereco(Endereco.builder().logradouro("log").bairro(null).build());
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_BAIRRO, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEndereco(Endereco.builder().logradouro(null).bairro("bairro").build());
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_LOGRADOURO, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEndereco(Endereco.builder().logradouro("log").bairro("bairro").localidade(null).build());
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_CIDADE, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEndereco(Endereco.builder().logradouro("log").bairro("bairro").localidade("local").uf(null).build());
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_UF, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEndereco(Endereco.builder().logradouro("log").bairro("bairro").localidade("local").uf("UF").cep(null).build());
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_CEP, NegocioException.class, () -> service.salvar(cliente));
		
		verifyNoInteractions(repository);
	}

	@Test
	void regraCamposTelefone() {
		Set<Telefone> telefones1 = new HashSet<>();
		telefones1.add(Telefone.builder().numero("33333333").tipo(null).ddd("66").build());
		
		Set<Telefone> telefones2 = new HashSet<>();
		telefones2.add(Telefone.builder().numero("33333333").tipo(TipoTelefoneEnum.CELULAR).ddd(null).build());
		
		Set<Telefone> telefones3 = new HashSet<>();
		telefones3.add(Telefone.builder().numero(null).tipo(TipoTelefoneEnum.CELULAR).ddd("66").build());
		
		
		cliente.setTelefones(telefones1);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_TIPO_TEL, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setTelefones(telefones2);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_DDD, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setTelefones(telefones3);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_NUM_TEL, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setTelefones(new HashSet<Telefone>(1));
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_TELEFONE, NegocioException.class, () -> service.salvar(cliente));
		
		verifyNoInteractions(repository);
	}

	@Test
	void regraCamposEmail() {
		Set<Email> emails1 = new HashSet<>(1);
		emails1.add(Email.builder().email(null).build());
		
		Set<Email> emails2 = new HashSet<>(1);
		emails2.add(Email.builder().email("teste").build());
		
		cliente.setEmails(emails1);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_EMAIL, NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEmails(emails2);
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.EMAIL_INVALIDO + ": " + "teste.email", NegocioException.class, () -> service.salvar(cliente));
		
		cliente.setEmails(new HashSet<>(1));
		when(repository.save(cliente)).thenReturn(cliente);
		assertThrows(Constantes.INFORME_EMAIL, NegocioException.class, () -> service.salvar(cliente));
		
		verifyNoInteractions(repository);
	}
	
	private Set<Telefone> getTelefones() {
		Set<Telefone> telefones = new HashSet<>(1);
		var tel = new Telefone(null, "(61)", "99999-9999", TipoTelefoneEnum.COMERCIAL, cliente, null);
		telefones.add(tel);
		return telefones;
	}
	
	private Set<Email> getEmails() {
		Set<Email> emails = new HashSet<>(1);
		emails.add(new Email(null, "teste@email.com", cliente, null));
		return emails;
	}
	
	private Endereco endereco() {
		return Endereco
				.builder() 
					.cep("00000-000")
					.bairro("Bairro")
					.localidade("Localidade")
					.logradouro("Logradouro")
					.uf("UF")
					.complemento("Complementos")
				.build();
	}

}
