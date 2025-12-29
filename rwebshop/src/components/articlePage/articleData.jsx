import react, { useState, useEffect } from "react"
import { useParams } from "react-router"
import { Navigate, Link } from "react-router-dom"
import axios from "axios"
import { Formik, Form, Field, FieldArray } from "formik"
import ArticleService from "../../API/ArticleService/ArticleService"
import DettListinoService from "../../API/DettListinoService/DettListinoService"
import IvaService from "../../API/IvaService/IvaService"
import FamAssortService from "../../API/FamAssortService/FamAssortService"
import ListiniService from "../../API/ListiniService/ListiniService"

const ArticleData = () =>{
    const cleanState={
        codart: "",
        descrizione: "",
        um: "",
        pzCart: "",
        pesoNetto: "",
        idfamass: -1,
        idiva: -1,
        stato: 1
    }

    const [formData, setFormData] = useState(cleanState);

    const [iva, setIva] = useState([]);
    const [famAss, setFamAss] = useState([]);
    const [listini, setListini] = useState([]);
    const [dettListini, setDettListini] = useState({});
    const [articolo, setArticolo] = useState({});

    const [title, setTitle] = useState("Inserisci");
    const [error, setError] = useState(false);
    const [toUpdate, setToUpdate] = useState(false);
    let params = useParams();

    useEffect(() => {
        //console.log(params.id);
        // IVA
        IvaService.getIva(setIva, setError);

        // FAMIGLIE ASSORTIMENTO
        FamAssortService.getFamAssort(setFamAss, setError);

        // LISTINI
        ListiniService.getListini(setListini, setError);

        //ottenimento info articolo se in modifica
        if(params.id!=null){
            setTitle("Aggiorna");
            ArticleService.getArticleById(params.id, setArticolo, setError);
        }

    }, []);

    useEffect(() => {
        if (!params?.id) return;

        DettListinoService.getDettListinoByCodart(params.id, listini, setDettListini, setError);

    }, [listini, params.id]);

    useEffect(() => {
        setFormData({
            ...formData,
            ...articolo
        })
    }, [articolo])

    function persist(values, { resetForm }){

        let dettListiniDict=values.dettListiniArray;
        let dettListiniArray=[];
        Object.entries(dettListiniDict).forEach(([key, value]) => {
            dettListiniArray.push({
                listino: key,
                id: value.id,
                prezzo: value.prezzo
            });
        });

        let insert=true;
        if(params.id!=null){
            insert=false;
            setToUpdate(true);
        }

        ArticleService.insertUpdateArticle(insert, values, dettListiniArray, setError);

        //reset campi
        let resetDettListini={};
        for(let listino in listini){
            resetDettListini[listini[listino].id]={...dettListini[listini[listino].id], prezzo:""};
        }
        setDettListini(resetDettListini);

        resetForm({
            values: {
                ...cleanState,
                dettListiniArray: resetDettListini
            }
        });
    }
    let codart=formData.codart, descrizione=formData.descrizione, um=formData.um, pzCart=formData.pzCart, pesoNetto=formData.pesoNetto,
    idfamass=formData.idfamass, idiva=formData.idiva, stato=formData.stato, dettListiniArray=dettListini;

    if(toUpdate){
        return <Navigate to="/home"/>
    }

    if(error){
        return <h1>Si è verificato un errore . . .</h1>
    }else{
        return (
            <div>
                <div className="row mt-2">
                    <div className="col">
                        <Link to="/home"><button className="btn btn-outline-secondary" type="button">Home</button></Link>
                    </div>
                </div>
                
                <div className="row">
                    <div className="col">
                        <Formik initialValues={{codart, descrizione, um, pzCart, pesoNetto, idfamass, idiva, stato, dettListiniArray}} enableReinitialize={true} onSubmit={persist}>
                            <Form>
                                <div className="row mt-3">
                                    <div className="col-4">
                                        <Field type="text" name="codart" id="codart" placeholder="Cod. Art" className="form-control"/>
                                    </div>
                                    <div className="col-6">
                                        <Field type="text" name="descrizione" id="descrizione" placeholder="Descrizione" className="form-control"/>
                                    </div>
                                </div>

                                <div className="row mt-2">
                                    <div className="col">
                                    <Field type="text" name="um" id="um" length={2} placeholder="UM" className="form-control"/>
                                    </div>
                                    <div className="col">
                                    <Field type="number" name="pzCart" id="pzCart" placeholder="PZ Cart" className="form-control"/>
                                    </div>

                                    <div className="col">
                                    <Field type="number" name="pesoNetto" id="pesoNetto" placeholder="Peso netto" className="form-control" step="0.001"/>
                                    </div>
                                </div>

                                <div className="row mt-2">
                                    <div className="col">
                                        <Field as="select" name="idiva" className="form-select">
                                            <option value="-1">SELEZIONA IVA</option>
                                            {
                                                iva.map(i => {
                                                    return <option key={i.idiva} value={i.idiva}>{i.descrizione}</option>
                                                })
                                            }
                                        </Field>
                                    </div>

                                    <div className="col">
                                        <Field as="select" name="idfamass" className="form-select">
                                            <option value="-1">SELEZIONA FAM. ASS.</option>
                                            {
                                                famAss.map(f => {
                                                    return <option key={f.id} value={f.id}>{f.descrizione}</option>
                                                })
                                            }
                                        </Field>
                                    </div>

                                    <div className="col">
                                        <Field as="select" name="stato" className="form-select">
                                            <option key={1} value="1">Valido</option>
                                            <option key={2} value="2">Sospeso</option>
                                            <option key={3} value="3">Eliminato</option>
                                        </Field>
                                    </div>
                                </div>
                                <br /><br />
                                {
                                    listini.map(l => {
                                        return (<div key={"d_"+l.id} className="row mt-4 justify-content-center">

                                            <div className="col-4">

                                            <p key={"p_"+l.id}>{l.descrizione}</p>
                                            <Field key={l.id} type="number" name={`dettListiniArray[${l.id}].prezzo`} step="0.001" className="form-control"/><br/>
                                            </div>
                                            </div>)
                                    })
                                }
                                <br />
                                <input type="submit" value={title} className="btn btn-outline-primary"/>
                            </Form>
                        </Formik>
                    </div>
                </div>
            </div>
        )
    }
}

export default ArticleData;


