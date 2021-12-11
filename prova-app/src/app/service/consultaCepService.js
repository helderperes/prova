import ApiService from "../apiservice";

export default class ConsultaCepService extends ApiService {

    constructor() {
        super('/api/cep/consulta');
    }
   
    buscarCep(cep) {
        return this.get(`/${cep}`)
    }

}