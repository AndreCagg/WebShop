import api from "../../API/Config/ConfigNetwork"

class IvaService{
    getIva(stateManager, errManager){
        api.get("http://localhost:8080/api/v1.0/iva", {withCredentials: true})
            .then(resp => stateManager(resp.data))
            .catch(err => errManager(true));
    }
}


export default new IvaService();