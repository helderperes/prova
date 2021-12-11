package br.com.prova.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.prova.dto.EnderecoDTO;
import br.com.prova.exception.NegocioException;
import br.com.prova.service.EnderecoService;
import br.com.prova.utils.Constantes;

@Service
public class EnderecoServiceImpl implements EnderecoService {


	@Override
	public Optional<EnderecoDTO> consultar(String cep) {
		if (StringUtils.isBlank(cep)) {
			throw new NegocioException(Constantes.INFORME_CEP);
		}
		var uri = "http://viacep.com.br/ws/{cep}/json/";
		
		Map<String, String> params = new HashMap<>();
		params.put("cep", cep);
		
		var dto = new RestTemplate().getForObject(uri, EnderecoDTO.class, params);
		return Optional.of(dto);
	}
}
