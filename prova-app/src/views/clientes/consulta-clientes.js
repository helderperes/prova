import React from 'react'
import { withRouter } from 'react-router-dom'

import Card from '../../components/card'
import FormGroup from '../../components/form-group'
import ClinteTable from './clientesTable'
import ClienteService from '../../app/service/clienteService'
import LocalStorageService from '../../app/service/localstorageService'

import * as messages from '../../components/toastr'

import {Dialog} from 'primereact/dialog';
import {Button} from 'primereact/button';

import CpfCnpj from '@react-br-forms/cpf-cnpj-mask'



class ConsultaCliente extends React.Component {

    state = {
        nome: '',
        cpfCnpj: '',
        showConfirmDialog: false,
        clienteEditar: {},
        clientes : []
    }

    constructor(){
        super();
        this.service = new ClienteService();
    }

    buscar = () => {
        if (!this.state.cpfCnpj && !this.state.nome) {
            messages.mensagemErro('Informe um campo para pesquisar.')
            return false;
        }
        
        const ursLogado = LocalStorageService.obterItem('_usuario_logado');

        const clienteFiltro = {
            nome: this.state.nome,
            cpfCnpj: this.state.cpfCnpj,
            usuario: ursLogado.id
        }

        this.service
            .consultar(clienteFiltro)
            .then( resposta => {
                const lista = resposta.data;
                
                if(lista.length < 1){
                    messages.mensagemAlert("Nenhum resultado encontrado.");
                }
                this.setState({ clientes: lista })
            }).catch( error => {
                console.log(error)
            })
    }

    editar = (id) => {
        this.props.history.push(`/cadastro-clientes/${id}`)
    }

    abrirConfirmacao = (cliente) => {
        this.setState({ showConfirmDialog : true, clienteDeletar: cliente  })
    }

    cancelarDelecao = () => {
        this.setState({ showConfirmDialog : false, clienteDeletar: {}  })
    }

    deletar = () => {
        this.service
            .deletar(this.state.clienteDeletar.id)
            .then(response => {
                const clientes = this.state.clientes;
                const index = clientes.indexOf(this.state.clienteDeletar)
                clientes.splice(index, 1);
                this.setState( { clientes: clientes, showConfirmDialog: false } )
                messages.mensagemSucesso('Cliente deletado com sucesso!')
            }).catch(error => {
                var resp = error.response.data
                if (resp.status === 403 || resp.status === 401) {
                    messages.mensagemErro('Você não tem permissão para deletar.')
                } else {
                    messages.mensagemErro('Ocorreu um erro ao tentar deletar o cliente')
                }
                console.log("Erro: ", error.response.data)                
            })
    }

    preparaFormularioCadastro = () => {
        this.props.history.push('/cadastro-clientes')
    }

    
    render() {

        const confirmDialogFooter = (
            <div>
                <Button label="Confirmar" icon="pi pi-check" onClick={this.deletar} />
                <Button label="Cancelar" icon="pi pi-times" onClick={this.cancelarDelecao} 
                        className="p-button-secondary" />
            </div>
        );

        return (
            <Card title="Consultar Clientes">
                <div className="row">
                    <div className="col-md-6">
                        <div className="bs-component">
                            <FormGroup htmlFor="inputNome" label="Nome: *">
                                <input type="text" 
                                       className="form-control" 
                                       id="inputNome" 
                                       value={this.state.nome}
                                       onChange={e => this.setState({nome: e.target.value})}/>
                            </FormGroup>

                            <FormGroup id="inputNome" label="CPF/CNPJ: *" >
                                <CpfCnpj
                                    className="form-control"
                                    name="cpfCnpj"
                                    type="tel"
                                    value={this.state.cpfCnpj}
                                    onChange={e => this.setState({cpfCnpj: e.target.value})}/>
                            </FormGroup>                            

                            <button onClick={this.buscar} 
                                    type="button" 
                                    className="btn btn-success">
                                    <i className="pi pi-search"></i> Buscar
                            </button>
                            <button onClick={this.preparaFormularioCadastro} 
                                    type="button" 
                                    className="btn btn-danger">
                                    <i className="pi pi-plus"></i> Cadastrar
                            </button>

                        </div>
                        
                    </div>
                </div>   
                <br/ >
                <div className="row">
                    <div className="col-md-12">
                        <div className="bs-component">
                            <ClinteTable clientes={this.state.clientes} 
                                              deleteAction={this.abrirConfirmacao}
                                              editAction={this.editar}/>
                        </div>
                    </div>  
                </div> 
                <div>
                    <Dialog header="Confirmação" 
                            visible={this.state.showConfirmDialog} 
                            style={{width: '50vw'}}
                            footer={confirmDialogFooter} 
                            modal={true} 
                            onHide={() => this.setState({showConfirmDialog: false})}>
                        Confirma a exclusão deste cliente?
                    </Dialog>
                </div>           
            </Card>

        )
    }
}

export default withRouter(ConsultaCliente);