import React from 'react'
import Card from '../components/card'
import FormGroup from '../components/form-group'
import { withRouter } from 'react-router-dom'

import UsuarioService from '../app/service/usuarioService'
import { mensagemErro } from '../components/toastr'
import { AuthContext  } from '../main/provedorAutenticacao'

class Login extends React.Component {

    state = {
        nome: '',
        senha: ''
    }

    constructor(){
        super();
        this.service = new UsuarioService();
    }

    entrar = () => {
        this.service.autenticar({
            nome: this.state.nome,
            senha: this.state.senha
        }).then( response => {
            this.context.iniciarSessao(response.data)
            this.props.history.push('/consulta-clientes')
        }).catch( erro => {
            console.log("Erro no login:", erro.response.data)
           mensagemErro(erro.response.data)
        })
    }

    prepareCadastrar = () => {
        this.props.history.push('/cadastro-usuarios')
    }

    render(){
        return (

            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <div className="bs-docs-section">
                        <Card title="Login">
                            <div className="row">
                                <div className="col-lg-12">
                                    <div className="bs-component">
                                        <fieldset>
                                            <FormGroup label="Nome: *" htmlFor="inputNome">
                                                <input type="nome" 
                                                    value={this.state.nome}
                                                    onChange={e => this.setState({nome: e.target.value})}
                                                    className="form-control" 
                                                    id="inputNome" 
                                                    aria-describedby="nomeHelp" 
                                                    placeholder="Digite o Nome" />
                                            </FormGroup>
                                            <FormGroup label="Senha: *" htmlFor="inputPassword1">
                                                <input type="password" 
                                                        value={this.state.senha}
                                                        onChange={e => this.setState({senha: e.target.value})}
                                                        className="form-control" 
                                                        id="inputPassword1" 
                                                        placeholder="Password" />
                                            </FormGroup>
                                            <button onClick={this.entrar} className="btn btn-success">
                                                <i className="pi pi-sign-in"></i>Entrar</button>
                                        </fieldset>
                                    </div>
                                </div>
                            </div>
                        </Card>
                    </div>
                </div>
            </div>

        )
    }
}

Login.contextType = AuthContext

export default withRouter( Login )