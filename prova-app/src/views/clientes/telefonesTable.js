import React from 'react'

export default props => {

    const rows = props.telefones.map( telefone => {
        return (            
            <tr key={telefone.key}>
                <td>{telefone.tipo}</td>
                <td>{telefone.ddd}</td>
                <td>{telefone.numero}</td>                
                <td>
                    <button type="button" title="Editar"
                            className="btn btn-primary"
                            onClick={e => props.editAction(telefone)}>
                            <i className="pi pi-pencil"></i>
                    </button>
                    <button type="button" title="Excluir"
                            className="btn btn-danger" 
                            onClick={ e => props.deleteAction(telefone)}>
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
                    <th scope="col">Tipo</th>
                    <th scope="col">DDD</th>
                    <th scope="col">Número</th>
                    <th scope="col">Ações</th>
                </tr>
            </thead>
            <tbody>
                {rows}
            </tbody>
        </table>
    )
}

