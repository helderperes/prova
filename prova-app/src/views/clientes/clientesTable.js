import React from 'react'

export default props => {

    const rows = props.clientes.map( cliente => {        
        return (
            <tr key={cliente.id}>
                <td>{cliente.nome}</td>
                <td>{cliente.cpfCnpj}</td>
                <td>
                    <button type="button"   title="Editar"
                            className="btn btn-primary"
                            onClick={e => props.editAction(cliente.id)}>
                            <i className="pi pi-pencil"></i>
                    </button>
                    <button type="button"  title="Excluir"
                            className="btn btn-danger" 
                            onClick={ e => props.deleteAction(cliente)}>
                            <i className="pi pi-trash"></i>
                    </button>
                </td>
            </tr>
        )
    } )

    return (
        <table className="table table-hover">
            <thead>
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">CPF/CNPJ</th>
                    <th scope="col">Ações</th>
                </tr>
            </thead>
            <tbody>
                {rows}
            </tbody>
        </table>
    )
}

