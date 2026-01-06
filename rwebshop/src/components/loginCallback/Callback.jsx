import React from "react"
import  { useContext, useEffect, useState } from "react"
import { useSearchParams, Navigate } from "react-router-dom";
import axios from "axios";
import { AuthContext } from "../../API/AuthService/Auth"

const Callback = () => {
    //return <h3>Callback page</h3>
    const [params, setParams] = useSearchParams();
    const { setTokens } = useContext(AuthContext);
    const [completed, setCompleted] = useState(null);

    //invio richiesta exchange token
    useEffect(() => {
        axios.post("http://localhost:8090/oauth2/token", {
        "grant_type": "authorization_code",
        "client_id": "client",
        "code": params.get("code"),
        "redirect_uri": "http://localhost:3000/callback",
        "code_verifier": "mm5Yz34kv8TO5410Z10z3zQQL7WxCAfvvUT_vLCqDGc"
        },
        {"headers": {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Basic "+btoa("client:secret")
        }}).then(resp => {
            console.log(resp.data["access_token"]);
            setTokens({
                "access_token": resp.data["access_token"],
                "refresh_token": resp.data["refresh_token"]
            });
            setCompleted(true);
        });
    }, []);

    if(completed){
        return <Navigate to="/home" />
    }
}

export default Callback;