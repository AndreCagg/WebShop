import api from "../../API/Config/ConfigNetwork"

class IvaService{
    getIva(stateManager, errManager){
        api.get("http://localhost:9090/api/v1.0/proxy/iva", {withCredentials: true})
            .then(resp => stateManager(resp.data))
            .catch(err => errManager(true));
    }
}


export default new IvaService();