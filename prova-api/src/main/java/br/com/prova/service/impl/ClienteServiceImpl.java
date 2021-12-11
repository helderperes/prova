package br.com.prova.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.com.prova.exception.NegocioException;
import br.com.prova.model.Cliente;
import br.com.prova.model.Email;
import br.com.prova.model.Endereco;
import br.com.prova.model.Telefone;
import br.com.prova.repository.ClienteRepository;
import br.com.prova.service.ClienteService;
import br.com.prova.utils.Constantes;

@Service
public class ClienteServiceImpl implements ClienteService {

	private static final String REGEX = "[^0-9]";
	
	
	@Autowired
	private ClienteRepository repository;
	
	
	@Override
	public Cliente salvar(Cliente cliente) {
		validar(cliente);
		return repository.save(cliente);
	}

	@Override
	public Cliente atualizar(Cliente cliente) {
		Objects.requireNonNull(cliente.getId());
		validar(cliente);
		return repository.save(cliente);
	}

	@Override
	public void deletar(Cliente cliente) {
		Objects.requireNonNull(cliente.getId());
		repository.delete(cliente);
	}

	@Override
	public List<Cliente> buscar(Cliente clienteFiltro) {
		var filtro = Example.of(clienteFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(filtro);
	}

	@Override
	public Optional<Cliente> obterPorId(Long id) {
		return repository.findById(id);
	}
	
	private void validar(Cliente cliente) {
		var nome = cliente.getNome();
		
		if (StringUtils.isBlank(nome)) {
			throw new NegocioException(Constantes.INFORME_NOME);
		}
		
		if (nome.length() < 3) {
			throw new NegocioException(Constantes.NOME_MIN_3_CARACT);
		}
		
		if (nome.length() > 100) {
			throw new NegocioException(Constantes.NOME_MAX_100_CARACT);
		}
		
		var cpfCnpj = Optional.ofNullable(cliente.getCpfCnpj()).filter(StringUtils::isNotBlank).map(p -> Pattern.compile(REGEX)
			.matcher(p).replaceAll("")).orElseThrow(() -> new NegocioException(Constantes.INFORME_CPF_CNPJ));
		
		cliente.setCpfCnpj(cpfCnpj);
		
		validarEndereco(cliente.getEndereco());
		validarTelefone(cliente.getTelefones());
		validarEmail(cliente.getEmails());
	}
	
	private void validarEndereco(Endereco e) {
		
		if (e == null) {
			throw new NegocioException(Constantes.INFORME_ENDERECO);
		}
		
		if (StringUtils.isBlank(e.getLogradouro())) {
			throw new NegocioException(Constantes.INFORME_LOGRADOURO);
		}
		
		if (StringUtils.isBlank(e.getBairro())) {
			throw new NegocioException(Constantes.INFORME_BAIRRO);
		}
		
		if (StringUtils.isBlank(e.getLocalidade())) {
			throw new NegocioException(Constantes.INFORME_CIDADE);
		}
		
		if (StringUtils.isBlank(e.getUf())) {
			throw new NegocioException(Constantes.INFORME_UF);
		}
		
		var cep = Optional.ofNullable(e.getCep()).filter(StringUtils::isNotBlank).map(p -> Pattern.compile(REGEX).matcher(p).replaceAll(""))
				.orElseThrow(() -> new NegocioException(Constantes.INFORME_CEP));
		
		e.setCep(cep);
	}
	
	private void validarTelefone(Set<Telefone> telefones) {
		
		if (CollectionUtils.isEmpty(telefones)) {
			throw new NegocioException(Constantes.INFORME_TELEFONE);
		}
		
		telefones.forEach(t -> {
			
			if (t.getTipo() == null) {
				throw new NegocioException(Constantes.INFORME_TIPO_TEL);
			}
			
			var ddd = Optional.ofNullable(t.getDdd()).filter(StringUtils::isNotBlank).map(p -> Pattern.compile(REGEX).matcher(p).replaceAll(""))
					.orElseThrow(() -> new NegocioException(Constantes.INFORME_DDD));
				
			var numero = Optional.ofNullable(t.getNumero()).filter(StringUtils::isNotBlank).map(p -> Pattern.compile(REGEX).matcher(p).replaceAll(""))
					.orElseThrow(() -> new NegocioException(Constantes.INFORME_NUM_TEL));
		
			t.setDdd(ddd);
			t.setNumero(numero);
		});
	}
	
	private void validarEmail(Set<Email> emails) {
		if (CollectionUtils.isEmpty(emails)) {
			throw new NegocioException(Constantes.INFORME_EMAIL);
		}
		
		var regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		var pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		emails.forEach(e -> {
			
			if (StringUtils.isBlank(e.getEmail())) {
				throw new NegocioException(Constantes.INFORME_EMAIL);
			} 
			
            var matcher = pattern.matcher(e.getEmail());
            if (!matcher.matches()) {
				throw new NegocioException(Constantes.EMAIL_INVALIDO + ": " + e.getEmail());
			}
		});
	}
	
}
