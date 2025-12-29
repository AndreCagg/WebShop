import ConfigNetwork from "../Config/ConfigNetwork";
import api from "../../API/Config/ConfigNetwork"



class ArticleService{


    getArticles(filter, status, limit, page, stateManager, errManager){
        let rows=parseInt(limit)==0?10:limit;
        return api.get("http://localhost:8080/api/v1.0/articoli", {
            params: {
                filter: filter,
                status: status,
                rows: rows,
                pageToGo: page
            },
            //withCredentials: true
        },).then(response => {
            stateManager(response.data);
            errManager(false);
        }).catch(err =>{
            errManager(true);
        });
    }

    getArticleById(id, stateManager, errManager){
        api.get("http://localhost:8080/api/v1.0/articoli/"+id).then(resp =>{
            let obj=resp.data;
            
            stateManager({
                codart: obj.codart,
                descrizione: obj.descrizione == null ? "" : obj.descrizione,
                um: obj.um == null ? "" : obj.um,
                pzCart: obj.pzcart == null ? "" : obj.pzcart,
                pesoNetto: obj.pesonetto == null ? "" : obj.pesonetto,
                idfamass: obj.idfamass == null ? "" : obj.idfamass,
                idiva: obj.idiva == null ? "" : obj.idiva,
                stato: obj.stato == null ? "" : obj.stato
            });
        }).catch(err =>{
            errManager(true);
        });
    }

    insertUpdateArticle(insert, values, dettListiniArray, errManager){
        let method="post";
        //console.log(values);
        if(!insert){
            method="patch";
        }

        api({
            method: method,
            url: "http://localhost:8080/api/v1.0/articoli",
            data: {
                codart: values.codart,
                descrizione: values.descrizione,
                um: values.um,
                pzcart: values.pzCart==""?0: values.pzCart,
                pesonetto: values.pesoNetto==""?0:values.pesoNetto,
                idfamass: values.idfamass,
                idiva: values.idiva,
                stato: values.stato,
                prezzoListini: dettListiniArray
            },
        })
        //.then(resp => resetForm())
        .catch(err => errManager(true));
    }

    deleteArticle(id, filter, status, rows, pagetogo, stateManager, errManager){
        api.delete("http://localhost:8080/api/v1.0/articoli/"+id).then(() => {
            this.getArticles(filter, status, rows, pagetogo, stateManager, errManager);
            errManager(false);
        }).catch(err => errManager(true));
    }

    setArticles(articles){
        this.articles=articles;
    }

    getArticlesData(){
        return this.articles;
    }
}


export default new ArticleService();