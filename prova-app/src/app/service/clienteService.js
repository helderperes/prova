import ApiService from "../apiservice";
import ErroValidacao from "../exception/ErroValidacao";

export default class ClienteService extends ApiService {

    constructor() {
        super('/api/cliente');
    }
   
    obterPorId(id) {
        return this.get(`/${id}`);
    }

    validar(cliente) {
        const erros = [];

        console.log("Entidade Cliente:", cliente)
        if (!cliente.nome) {
            erros.push("Informe o Nome.")
        }

        if (!cliente.cpfCnpj) {
            erros.push("Informe o CPF/CNPJ.")
        }

        if (!cliente.endereco) {
            erros.push("Informe um Endereço.")
        }

        if (!cliente.endereco.cep) {
            erros.push("Informe o CEP.")
        }

        if (!cliente.endereco.bairro) {
            erros.push("Informe o Bairro.")
        }

        if (!cliente.endereco.localidade) {
            erros.push("Informe a Cidade.")
        }

        if (!cliente.endereco.logradouro) {
            erros.push("Informe o Logradouro.")
        }

        if (!cliente.endereco.uf) {
            erros.push("Informe a UF.")
        } 

        if (cliente.emails && cliente.emails.length < 1) {
            erros.push('Informe um Email.')
        } else {
            const emails = cliente.emails;
            emails.forEach(e => {
                if (!e.email.match(/^[a-z0-9.]+@[a-z0-9]+\.[a-z]/)) {
                    const mensagem = 'O email informado é invalido: ';
                    erros.push(mensagem.concat(e.email));
                }
            });            
        }

        if (cliente.telefones && cliente.telefones.length < 1) {
            erros.push('Informe um Telefone.')
        } else {
            const telefones = cliente.telefones;
            telefones.forEach(e => {
                if (!e.ddd) {
                    erros.push('Informe o DDD.')
                }
                if (!e.tipo) {
                    erros.push('Informe o Tipo de Telefone.')
                }
                if (!e.numero) {
                    erros.push('Informe o Número de Telefone.')
                }
            });            
        }
        
        if (erros && erros.length > 0) {
            throw new ErroValidacao(erros);
        }
        
    }

    salvar(cliente) {
        return this.post('/', cliente);
    }

    atualizar(cliente) {
        return this.put(`/${cliente.id}`, cliente);
    }

    consultar(clienteFiltro) {
        let params = `?nome=${clienteFiltro.nome}`

        if (clienteFiltro.cpfcnpj) {
            params = `${params}&cpfcnpj=${clienteFiltro.cpfcnpj}`
        }
        return this.get(params);
    }

    deletar(id) {
        return this.delete(`/${id}`)
    }
}