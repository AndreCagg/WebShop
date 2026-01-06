import api from "../../API/Config/ConfigNetwork"

class FamAssortService{
    getFamAssort(stateManager, errManager){
        api.get("http://localhost:9090/api/v1.0/proxy/famass", {withCredentials: true})
            .then(resp => stateManager(resp.data))
            .catch(err => errManager(true));
    }
}

export default new FamAssortService();