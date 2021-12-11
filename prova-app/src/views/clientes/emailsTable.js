import React from 'react'

export default props => {

    const rows = props.emails.map( em => {
        return (
            <tr key={em.key}>
                <td>{em.email}</td>
                <td>
                    <button type="button" title="Editar"
                            className="btn btn-primary"
                            onClick={e => props.editAction(em)}>
                            <i className="pi pi-pencil"></i>
                    </button>
                    <button type="button" title="Excluir"
                            className="btn btn-danger" 
                            onClick={ e => props.deleteAction(em)}>
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
                    <th scope="col">Email</th>
                    <th scope="col">Ações</th>
                </tr>
            </thead>
            <tbody>
                {rows}
            </tbody>
        </table>
    )
}

