import axios from "axios";

class ArticleService{


    getArticles(filter, status, limit, page, stateManager){
        let rows=parseInt(limit)==0?10:limit;
        axios.get("http://localhost:8080/api/v1.0/articoli", {
            headers: {"Authorization": "Bearer "+localStorage.getItem("token")},
            params: {
                filter: filter,
                status: status,
                rows: rows,
                pageToGo: page
            }
        }).then(response => {
            stateManager(response.data);
        });
    }

    deleteArticle(id, stateManager){
        axios.delete("http://localhost:8080/api/v1.0/articoli/"+id,{
            headers: {"Authorization": "Bearer "+localStorage.getItem("token")}
        }).then(() =>
            this.getArticles("", 0, 10, 0, stateManager)
        );
    }

    setArticles(articles){
        this.articles=articles;
    }

    getArticlesData(){
        return this.articles;
    }
}


export default new ArticleService();