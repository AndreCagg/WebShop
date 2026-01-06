import React, { useState, useEffect, useContext } from "react"
import { Navigate, Outlet } from "react-router-dom"
import api from "../../API/Config/ConfigNetwork"
import { AuthContext } from "../../API/AuthService/Auth"

const PrivateRoutes = () => {
  const { tokens } = useContext(AuthContext);
  const [auth, setAuth] = useState(null);

  useEffect(() => {
    isLogged();
  }, []);

  async function isLogged(){
    await api.get("http://localhost:9090/api/v1.0/logged", { withCredentials: true }).then(resp => {
      
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

  /*if(tokens["access_token"]==""){
    return <Navigate to="/"/>
  }else{
    return <Outlet />
  }*/

  //return <Outlet />
}

export default PrivateRoutes;