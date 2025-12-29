import api from "../../API/Config/ConfigNetwork"


class DettListinoService{
    getDettListinoByCodart(id, listini, stateManager, errManager){
        api.get("http://localhost:8080/api/v1.0/dettlistini/"+id, { withCredentials: true })
            .then(resp => {
                const nuoviDettListini = {};

                //associa dettlistini e listini
                listini.forEach(lst => {
                    console.log(resp.data);
                    const dict = this.cercaDettlistino(lst.id, resp.data);
                    let obj={prezzo: dict["prezzo"]};

                    if(dict.prezzo!=""){
                        obj["id"]=dict.id;
                    }
                    nuoviDettListini[lst.id] = obj;
                });

                stateManager(nuoviDettListini);
            })
            .catch(err => {
                console.log(err);
                errManager(true);
            });
    }

    cercaDettlistino(listinoId, lista) {
        if(lista!=""){
            const trovato = lista.find(d => d.listino === listinoId);

            if (trovato) {
                return {
                    prezzo: trovato.prezzo,
                    id: trovato.id
                };
            }
        }

        return {
            prezzo: "",
            id: listinoId
        };
    }
}

export default new DettListinoService();