import React from "react";
import { useState } from "react";

import ArticleTable from "./components/articleTable/ArticleTable";
import FilterForm from "./components/homePage/FilterForm";

/*
    filterform acquisisce i valori e setta lo stato
    articletable visualizza lo stato
*/


const Page = (props) =>{
    const [formData, setFormData] = useState({
        "filter": "",
        "status": 0,
        "rows": 10,
        "page": props.page
    });
    
    function reloadArt(event){
        setFormData({...formData, [event.target.name] : 
            (event.target.name=="rows" || event.target.name=="status") ? parseInt(event.target.value): event.target.value});
    }

    function refreshPage(op){
        if(op=="add"){
            //controllare che non supera il massimo
            setFormData({...formData, "page": formData.page+1});
        }else{
            let page=0;
            if(formData.page>=1){
                page=formData.page-=1;
            }

            setFormData({...formData, "page": page});  
        }
    }
    
    return (
        <div className="row">
            <div className="col">
                <FilterForm refreshMethod={reloadArt} getNewPage={refreshPage}></FilterForm>


                <ArticleTable
                filter={formData.filter}
                status={formData.status}
                rows={formData.rows}
                pageToGo={formData.page}></ArticleTable>
            </div>
        </div>
    );
}

export default Page;