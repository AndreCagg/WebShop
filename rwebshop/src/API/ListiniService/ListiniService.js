import api from "../../API/Config/ConfigNetwork"

class ListiniService{
    getListini(stateManager, errManager){
        api.get("http://localhost:8080/api/v1.0/listini")
        .then(resp => stateManager(resp.data))
        .catch(err => errManager(true));
    }
}

export default new ListiniService();