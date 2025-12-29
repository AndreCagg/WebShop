import React from "react"
import ConfirmPopup from "./Popup"
import { Link } from "react-router-dom"


const row = (props) => {
    return (
        <tr>
            <td>{props.codart}</td>
            <td>{props.descrizione}</td>
            <td>{props.stato}</td>
            <td>{props.prezzo}</td>
            <td><ConfirmPopup onDelete={props.onDelete} id={props.codart}/></td>
            <td><Link to={"/modifica/"+props.codart}><button className="btn btn-outline-secondary btn-sm">Modifica</button></Link></td>
        </tr>
    );
}

export default row;