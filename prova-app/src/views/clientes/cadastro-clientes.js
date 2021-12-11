import React from 'react'

import Card from '../../components/card'
import FormGroup from '../../components/form-group'
import TelefoneTable from '../clientes/telefonesTable'
import EmailTable from '../clientes/emailsTable'
import InputMask from "react-input-mask";

import { withRouter } from 'react-router-dom'
import * as messages from '../../components/toastr'

import ClienteService from '../../app/service/clienteService'
import ConsultaCepService from '../../app/service/consultaCepService'

import LocalStorageService from '../../app/service/localstorageService'

import CpfCnpj from '@react-br-forms/cpf-cnpj-mask'

import {Dialog} from 'primereact/dialog'
import {Button} from 'primereact/button'


class CadastroCliente extends React.Component {

    state = {
        id: null,
        nome: '',
        cpfCnpj: '',
        endereco : '',
        email : '', 
        tipo : '',        
        cep: '', 
        bairro: '', 
        localidade: '', 
        logradouro: '',
        uf: '', 
        complemento: '',
        ddd: '',
        numero: '',
        emailAlteracao: null, 
        telAlteracao: null,
        abrirModalTel : false, 
        abrirModalEmail: false,
        mostrarInput : true, 
        telefones : [],
        emails : []        
    }

    constructor() {
        super();
        this.service = new ClienteService();
        this.cepService = new ConsultaCepService();
    }

    componentDidMount() {
        const params = this.props.match.params        
        if (params.id) {            
            this.service
                .obterPorId(params.id)
                .then(resp => {  
                    const e = resp.data.endereco
                    const emails = resp.data.emails
                    const telefones = resp.data.telefones
                   
                    for (let i = 0; i < telefones.length; i++) {
                        let tel = telefones[i]
                        let index = telefones.indexOf(tel)
                        tel.key = index
                    }

                    for (let i = 0; i < emails.length; i++) {
                        let e = emails[i]
                        let index = emails.indexOf(e)
                        e.key = index
                    }

                    this.setState({
                        id: resp.data.id,
                        nome: resp.data.nome,
                        cpfCnpj: resp.data.cpfCnpj,
                        cep: e.cep, 
                        bairro: e.bairro, 
                        localidade: e.localidade, 
                        logradouro: e.logradouro,
                        uf: e.uf, 
                        complemento: e.complemento,
                        atualizando: true,
                        emails: emails,
                        telefones:telefones
                    }) 
                })
                .catch(erros => {
                    messages.mensagemErro(erros.response.data)
                })
        }
    }

    submit = () => {
        const ursLogado = LocalStorageService.obterItem('_usuario_logado')        
        const { nome, cpfCnpj, telefones, emails, cep, bairro, localidade, logradouro, uf, complemento, id  } = this.state; 
        const endereco = { cep, bairro, localidade, logradouro, uf, complemento };
        const cliente = { nome, cpfCnpj, endereco, telefones, emails, usuario: ursLogado.id, id };
                        
        try {
            this.service.validar(cliente)
        } catch(erro) {
            const mensagens = erro.mensagens;
            mensagens.forEach(msg => messages.mensagemErro(msg));
            return false;
        }     

        this.service
            .salvar(cliente)
            .then(response => {
                this.props.history.push('/consulta-clientes')
                messages.mensagemSucesso('Cliente cadastrado com sucesso!')
            }).catch(error => {
                var resp = error.response.data
                if (resp.status === 403 || resp.status === 401) {
                    messages.mensagemErro('Você não tem permissão para incluir.')
                } else {
                    messages.mensagemErro('Ocorreu um erro ao tentar salvar o cliente')
                }
                console.log("Erro: ", error.response.data)
            })
    }

    atualizar = () => {
        const ursLogado = LocalStorageService.obterItem('_usuario_logado')    
        const { nome, cpfCnpj, telefones, emails, cep, bairro, localidade, logradouro, uf, complemento, id  } = this.state; 
        const endereco = { cep, bairro, localidade, logradouro, uf, complemento };
        const cliente = { nome, cpfCnpj, endereco, telefones, emails, usuario: ursLogado.id, id };
        
        try {
            this.service.validar(cliente)
        } catch(erro) {
            const mensagens = erro.mensagens;
            mensagens.forEach(msg => messages.mensagemErro(msg));
            return false;
        } 
        
        this.service
            .atualizar(cliente)
            .then(response => {
                this.props.history.push('/consulta-clientes')
                messages.mensagemSucesso('Cliente atualizado com sucesso!')
            }).catch(error => {
                const resp = error.response.data;
                if (resp.status === 403 || resp.status === 401) {
                    messages.mensagemErro('Você não tem permissão para alterar.')
                } else {
                    messages.mensagemErro('Ocorreu um erro ao tentar atualizar o cliente')
                }
                console.log("Erro: ", error.response.data)
                
            })
    }

    handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        this.setState({ [name] : value })
    }    

    addTelefone = () => {
        let mensagem = 'Telefone adicionado com sucesso!'
        let telefones = []
        let index = ''
        const { ddd, numero, tipo, key=0 } = this.state
        const telefone = { ddd, numero, tipo, key}
        const listaTemp = this.state.telefones
        
        if (this.state.telAlteracao) {
            index = listaTemp.indexOf(this.state.telAlteracao)
            listaTemp.splice(index, 1); 
            telefones = listaTemp.concat(telefone)  
            mensagem = 'Telefone alterado com sucesso!'  
            for(let i = 0; i < telefones.length; i++) {
                let tel = telefones[i]
                index = telefones.indexOf(tel)
                tel.key = index
            }            
        } else if (listaTemp.length > 0) {
            telefones = listaTemp.concat(telefone) 
            for(let i = 0; i < telefones.length; i++) {
                let tel = telefones[i]
                index = telefones.indexOf(tel)
                tel.key = index
            }            
        } else {
            telefones = listaTemp.concat(telefone)
        }  
        this.setState({ telefones: telefones, ddd:'', numero:'', tipo:'', telAlteracao:'' })      
        messages.mensagemSucesso(mensagem)
    }

    addEmail = () => {	                                                      
        let mensagem = 'Email adicionado com sucesso!'    
        let emails = []    
        let index = ''    
        const { email, key=0 } = this.state;    
        const em = { email, key };    
        const listaTemp = this.state.emails 	
           
        if (this.state.emailAlteracao) {                         
            index = listaTemp.indexOf(this.state.emailAlteracao)    
            listaTemp.splice(index, 1);    
            emails = listaTemp.concat(em)                 
            mensagem = 'Email alterado com sucesso!'    
            for(let i = 0; i < emails.length; i++) {                           
                let e = emails[i]                                              
                index = emails.indexOf(e)                                      
                e.key = index                                                  
            }                                                                  
        } else if (listaTemp.length > 0) {                                     
            emails = listaTemp.concat(em)                                      
            for(let i = 0; i < emails.length; i++) {                           
                let e = emails[i]                                              
                index = emails.indexOf(e)                                    
                e.key = index                                                  
            }                                                                  
        } else {
            emails = listaTemp.concat(em)
        }                                                                     
        this.setState({ emails: emails, email:'', emailAlteracao:'' })         
        messages.mensagemSucesso(mensagem)                                     
   } 

    editarTelefone = (tel) => {
        let isCel = false;
        if ('CELULAR' === tel.tipo) { 
            isCel = true
        } 
       this.setState({ 
           idTelefone: tel.id, 
           numero: tel.numero, 
           tipo: tel.tipo, 
           ddd: tel.ddd, 
           abrirModalTel:true, 
           telAlteracao:tel,
           mostrarInput:isCel 
        })  
    }

    deletarTelefone = (telefone) => {
        let key = ''
        const telefones = this.state.telefones;
        const index = telefones.indexOf(telefone)
        telefones.splice(index, 1);
        key = (telefones.length) + 1
        this.setState({ telefones: telefones, showConfirmDialog: false, key})
        messages.mensagemSucesso('Telefone deletado com sucesso!')
    }    
    
    editarEmail = (email) => {
        this.setState({
            abrirModalEmail: true, 
            idEmail: email.id,
            emailAlteracao: email, 
            email: email.email
        }) 
    }

    deletarEmail = (email) => {
        const emails = this.state.emails;
        const index = emails.indexOf(email)
        emails.splice(index, 1);
        this.setState( { emails: emails, showConfirmDialog: false } )
        messages.mensagemSucesso('Email deletado com sucesso!')
    }

    mudarInput = (event) => {
        const tipo = event.target.value
        if ('CELULAR' === tipo) { 
            this.setState({ tipo: tipo, mostrarInput:true})  
        } else {
            this.setState({ tipo: tipo, mostrarInput:false})
        }        
    }

    buscarCep = () => {   
    const cep = this.state.cep  
    if (!cep) {
        messages.mensagemErro('Informe o CEP.')
        return
    }   
    this.cepService.buscarCep(cep)
        .then(resp => {
            var e = resp.data
            this.setState({
                cpe: e.cep, 
                bairro: e.bairro, 
                localidade: e.localidade, 
                logradouro: e.logradouro,
                uf: e.uf, 
                complemento: e.complemento
            })
        }).catch(error => {            
            messages.mensagemErro(error.response.data)
        }) 
    }

    render() {

        const confirmAddTel = (
            <div>
                <Button label="Confirmar" icon="pi pi-check" onClick={this.addTelefone} />
                <Button label="Cancelar" icon="pi pi-times" onClick={() => this.setState({abrirModalTel:false})} 
                    className="p-button-secondary" />
            </div>
        );       
        const confirmAddEmail = (
            <div>
                <Button label="Confirmar" icon="pi pi-check" onClick={this.addEmail} />
                <Button label="Cancelar" icon="pi pi-times" onClick={() => this.setState({abrirModalEmail:false})} 
                 className="p-button-secondary" />
            </div>
        );

        return (
            <Card title={ this.state.id ? 'Atualizar'  : 'Cadastrar' }>
                <h5 className="card-title">Cliente:</h5>
                <div className="row">
                    <div className="form-group col-md-6">
                        <FormGroup id="inputNome" label="Nome: *" >
                            <input id="inputNome" type="text" 
                                   className="form-control" 
                                   name="nome"
                                   value={this.state.nome}
                                   onChange={this.handleChange}/>
                        </FormGroup>                        
                    </div>
                    <div className="form-group col-md-6">
                        <FormGroup id="inputNome" label="CPF/CNPJ: *" >
                            <CpfCnpj
                                className="form-control"
                                name="cpfCnpj"
                                type="tel"
                                value={this.state.cpfCnpj}
                                onChange={this.handleChange}/>                         
                        </FormGroup>           
                    </div>
                </div>
                <h5 className="card-title">Endereço:</h5>
                <div className="row">
                    <div className="form-group col-md-4">
                        <FormGroup id="inputCep" label="CEP: *" >
                            <InputMask mask="99999-999"
                                id="inputCep"
                                className="form-control"
                                name="cep"
                                value={this.state.cep}
                                onChange={this.handleChange}/>                          
                        </FormGroup>                        
                    </div>
                    <div className="form-group col-md-2">
                        <button onClick={this.buscarCep} 
                                className="btn btn-success"
                                style={styles.marginButto}>
                                <i className="pi pi-refresh"></i> Pesquisar
                        </button>                      
                    </div>
                </div>
                <div className="row">
                    <div className="form-group col-md-6">
                        <FormGroup id="inputBairro" label="Bairro: *" >
                            <input id="inputBairro" type="text" 
                                   className="form-control" 
                                   name="bairro"
                                   value={this.state.bairro}
                                   onChange={this.handleChange}/>                          
                        </FormGroup>                        
                    </div>
                    <div className="form-group col-md-6">
                        <FormGroup id="inputLogradouro" label="Logradouro: *" >
                            <input id="inputLogradouro" type="text" 
                                   className="form-control" 
                                   name="logradouro"
                                   value={this.state.logradouro}
                                   onChange={this.handleChange}/>                          
                        </FormGroup>                      
                    </div>
                </div>
                <div className="row">
                    <div className="form-group col-md-6">
                        <FormGroup id="inputCidade" label="Cidade: *" >
                            <input id="inputCidade" type="text" 
                                   className="form-control" 
                                   name="localidade"
                                   value={this.state.localidade}
                                   onChange={this.handleChange}/>                          
                        </FormGroup>                        
                    </div>
                    <div className="form-group col-md-6">
                        <FormGroup id="inputUf" label="UF: *" >
                            <input id="inputUf" type="text" 
                                   className="form-control" 
                                   name="uf"
                                   value={this.state.uf}
                                   onChange={this.handleChange}/>                          
                        </FormGroup>                      
                    </div>
                </div>
                <div className="row">
                    <div className="form-group col-md-6">
                        <FormGroup id="inputOutros" label="Complementos:" >
                            <input id="inputOutros" type="text" 
                                   className="form-control" 
                                   name="complemento"
                                   value={this.state.complemento}
                                   onChange={this.handleChange}/>                          
                        </FormGroup>                        
                    </div>
                </div>
                <br/>
                <h5 className="card-title">Telefones:</h5>
                <div className="row">
                    <div className="col-md-12">
                        <div className="bs-component">
                            <TelefoneTable telefones={this.state.telefones} 
                                              deleteAction={this.deletarTelefone}
                                              editAction={this.editarTelefone}/>
                        </div>
                    </div>  
                </div> 
                <br/>
                <h5 className="card-title">Emails:</h5>
                <div className="row">
                    <div className="col-md-12">
                        <div className="bs-component">
                            <EmailTable emails={this.state.emails} 
                                              deleteAction={this.deletarEmail}
                                              editAction={this.editarEmail}/>
                        </div>
                    </div>  
                </div> 
                <div className="row">
                     <div className="col-md-6" >
                        { this.state.id ? 
                            (
                                <button onClick={this.atualizar} 
                                        className="btn btn-success">
                                        <i className="pi pi-refresh"></i> Atualizar
                                </button>
                            ) : (
                                <button onClick={this.submit} 
                                        className="btn btn-success">
                                        <i className="pi pi-save"></i> Salvar
                                </button>
                            )
                        }
                        <button onClick={e => this.props.history.push('/consulta-clientes')} 
                                className="btn btn-danger">
                                <i className="pi pi-times"></i>Cancelar
                        </button>
                        <button type="button" 
                                className="btn btn-link" 
                                data-toggle="modal" 
                                onClick={() => this.setState({abrirModalTel:true, ddd:'', numero:'', tipo:'', telAlteracao:''})}>Cadastrar Telefone
                        </button>
                        <button type="button" 
                                className="btn btn-link" 
                                onClick={() => this.setState({abrirModalEmail:true})}>Cadastrar Email
                        </button>
                    </div>
                </div>
                <Dialog header={ this.state.id ? 'Atualizar'  : 'Cadastrar' }
                            visible={this.state.abrirModalTel} 
                            style={{width: '50vw'}}
                            footer={confirmAddTel} 
                            modal={true}                             
                            onHide={() => this.setState({abrirModalTel: false})}>
                        <div className="form-group">
                            <div className="form-group col-md-6">
                                <FormGroup htmlFor="inputTipo" label="Tipo: ">                                    
                                    <select className="form-control" value={this.state.tipo} onChange={this.mudarInput}>
                                        <option value="">SELECIONE</option>
                                        <option value="CELULAR">CELULAR</option>
                                        <option value="RESIDENCIAL">RESIDENCIAL</option>
                                        <option value="COMERCIAL">COMERCIAL</option>
                                    </select> 
                                </FormGroup>
                                <FormGroup id="inputDDD" label="DDD: *" >
                                    <input id="inputDDD" type="text" 
                                        className="form-control" 
                                        name="ddd"
                                        value={this.state.ddd}
                                        onChange={this.handleChange}/>                        
                                </FormGroup>                        
                            </div>                            
                             <div className="form-group col-md-6">
                                { this.state.mostrarInput ? 
                                    (
                                        <FormGroup id="inputCelular" label="Número: *" >
                                            <InputMask mask="9 9999-9999"
                                                id="inputCelular"
                                                className="form-control"
                                                name="numero"
                                                value={this.state.numero}
                                                onChange={this.handleChange}/>  
                                        </FormGroup>                                        
                                    ) : (
                                        <FormGroup id="inputFixo" label="Número: *" >
                                            <InputMask mask="9999-9999"
                                                id="inputFixo"
                                                className="form-control"
                                                name="numero"
                                                value={this.state.numero}
                                                onChange={this.handleChange}/>                                            
                                        </FormGroup>  
                                    )
                                }                                                        
                            </div>
                        </div>  
                </Dialog> 
                <Dialog header={ this.state.id ? 'Atualizar'  : 'Cadastrar' } 
                            visible={this.state.abrirModalEmail} 
                            style={{width: '50vw'}}
                            footer={confirmAddEmail} 
                            modal={true} 
                            onHide={() => this.setState({ abrirModalEmail: false })}>
                        
                            <div className="form-group col-md-6">
                                    <FormGroup id="inputEmail" label="Email: *" >
                                        <input id="inputEmail" type="text" 
                                            className="form-control" 
                                            name="email"
                                            value={this.state.email}
                                            onChange={this.handleChange}/>                        
                                    </FormGroup>                        
                            </div>                                
                         
                </Dialog>                
            </Card>
        )
    }
}

var styles = {
    marginButto: {
        marginTop: '30px',
        marginLeft: '1px'
    }
};
export default withRouter(CadastroCliente);