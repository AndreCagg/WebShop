import React, { useState, useEffect } from "react"
import { Navigate, Outlet } from "react-router-dom"
import api from "../../API/Config/ConfigNetwork"

const PrivateRoutes = () => {
  const [auth, setAuth] = useState(null);

  useEffect(() => {
    isLogged();
  }, []);

  async function isLogged(){
    await api.get("http://localhost:8090/api/v1.0/utenti/isLogged", { withCredentials: true }).then(resp => {
      
      if(resp.status == 200){
        setAuth(true);
      }else{
        setAuth(false);
      }

    }).catch(err => setAuth(false));
  }
  //console.log(" - "+auth);
  if(auth==null){
    return <h2>Attendere . . .</h2>
  }else{
    return (
      auth ? <Outlet/> : <Navigate to='/'/>
    )
  }
}

export default PrivateRoutes;