import React from 'react'

import Login from '../views/login'
import CadastroCliente from '../views/clientes/cadastro-clientes'
import ConsultaClientes from '../views/clientes/consulta-clientes'
import { AuthConsumer } from '../main/provedorAutenticacao'

import { Route, Switch, BrowserRouter, Redirect } from 'react-router-dom'

function RotaAutenticada( { component: Component, isUsuarioAutenticado, ...props } ) {
    return (
        <Route exact {...props} render={ (componentProps) => {
            if(isUsuarioAutenticado){
                return (
                    <Component {...componentProps} />
                )
            }else{
                return(
                    <Redirect to={ {pathname : '/login', state : { from: componentProps.location } } } />
                )
            }
        }}  />
    )
}

function Rotas(props) {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" component={Login} />
                <Route exact path="/login" component={Login} />
                                
                <RotaAutenticada isUsuarioAutenticado={props.isUsuarioAutenticado} path="/consulta-clientes" component={ConsultaClientes} />                
                <RotaAutenticada isUsuarioAutenticado={props.isUsuarioAutenticado} path="/cadastro-clientes/:id?" component={CadastroCliente} />
            </Switch>
        </BrowserRouter>
    )
}

export default () => (
    <AuthConsumer>
        { (context) => (<Rotas isUsuarioAutenticado={context.isAutenticado} />) }
    </AuthConsumer>
)