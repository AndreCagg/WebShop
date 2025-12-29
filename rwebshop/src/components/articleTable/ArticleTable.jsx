import React from "react";
import { useState } from "react"
import { useEffect } from "react"
import Row from "./Row";
import ArticleService from "../../API/ArticleService/ArticleService";


const ArticleTable = (props) =>{
    const [articles, setArticles] = useState([]);
    const [error, setError] = useState(false);

    useEffect(() => {

        ArticleService.getArticles(props.filter, props.status, props.rows, props.pageToGo, setArticles, setError);

    }, [props.filter, props.status, props.rows, props.pageToGo]);

    useEffect(() => {

        ArticleService.getArticles("", 0, 10, 0, setArticles, setError);

    }, []);

    function eliminaArt(id){
        ArticleService.deleteArticle(id, props.filter, props.status, props.rows, props.pageToGo, setArticles, setError);
    }

    if(error){
        return <h1>Si è verificato un errore</h1>
    }else{
        return (
                <div className="mt-4">
                    <table>
                        <thead>
                            <tr>
                                <th>Cod. Art</th>
                                <th>Descrizione</th>
                                <th>Stato</th>
                                <th>Prezzo</th>
                                <th></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {articles.map(art => {
                                let prezzo=Math.min(...art.prezzoListini.map(l => parseFloat(l.prezzo)));
                                if(prezzo==Infinity){
                                    prezzo="";
                                }

                                return <Row key={art.codart} 
                                codart={art.codart} 
                                descrizione={art.descrizione}
                                stato={art.stato}
                                prezzo={prezzo} onDelete={eliminaArt} onEditArticle={setArticles} onErrorTable={setError}></Row>
                            })}
                        </tbody>
                    </table>
                </div>
            );
    }
}

export default ArticleTable;