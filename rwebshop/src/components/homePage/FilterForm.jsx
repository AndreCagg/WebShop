import React from "react"
import { Link } from "react-router-dom"


const FilterForm = (props) => {
    function preventSubmit(event){
        event.preventDefault();
    }
    return (
        <div>
                <form onSubmit={preventSubmit}>
                    <input name="filter" type="text" placeholder="Cerca per codice o descrizione" onChange={props.refreshMethod} className="form-control mt-2"/>
                    <select name="status" onChange={props.refreshMethod} className="form-select mt-1">
                        <option value="0">STATO</option>
                        <option value="1">Valido</option>
                        <option value="2">Sospeso</option>
                        <option value="3">Eliminato</option>
                    </select>
                    <select name="rows" onChange={props.refreshMethod} className="form-select mt-1">
                        <option value="0">RIGHE</option>
                        <option value="10">10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                    </select><br />
                    <button type="button" className="btn btn-secondary me-1" onClick={props.getNewPage.bind(this,"remove")}>&larr;</button><button type="button" className="btn btn-secondary" onClick={props.getNewPage.bind(this,"add")}>&rarr;</button>
                    <Link to="/inserisci"><button type="button" className="ms-4 btn btn-outline-secondary">+ AGGIUNGI</button></Link>
                </form>
            
            
        </div>
    )
}

export default FilterForm;