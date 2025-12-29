import React from "react"
import Popup from "reactjs-popup"
import 'reactjs-popup/dist/index.css';

const ConfirmPopup = (props) =>{

    return (
        <div>
            <Popup trigger={<button className="btn btn-outline-danger btn-sm">Elimina</button>} modal nested>
            {
                close => (
                    <div className="confirm-popup">
                        <div className="content">
                            Sei sicuro di voler eliminare l'articolo?<br /><br />
                        </div>
                        <div className="row">
                            <div className="col-1 me-2">
                                <button className="btn btn-outline-primary" onClick=
                                    {() => close()}>
                                        Annulla
                                </button> &nbsp;
                            </div>

                            <div className="col-1 ms-4">
                                <button className="btn btn-outline-danger" onClick={() => {
                                    props.onDelete(props.id);
                                    close();
                                    }}>Elimina
                                </button>
                            </div>
                        </div>
                    </div>
                )
            }
            </Popup>
        </div>
    )
}

export default ConfirmPopup;