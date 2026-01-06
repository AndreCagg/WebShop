import './App.css';
import Page from "./Page"
import LoginPage from "./components/login/loginPage";
import CallbackPage from "./components/loginCallback/Callback"
import ArticleData from "./components/articlePage/articleData"
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import PrivateAuth from "./components/PrivateAuth/PrivateAuth";
import { AuthContext } from "../src/API/AuthService/Auth"
import { setTokens } from "./API/Config/ConfigNetwork"
import { useEffect, useContext } from "react"

function App() {

  const { tokens } = useContext(AuthContext);
  useEffect(() => {
    setTokens(tokens);
  }, [tokens]);
  

  return (
    <div className="App">
      <BrowserRouter>
        <div className="container d-flex justify-content-center">
          <Routes>
            <Route path="/" exact element={<Navigate to="http://localhost:9090/api/v1.0/login" />}/>

            
            <Route element={<PrivateAuth/>}>
              <Route path="/home" element={<Page page={0} />} />
              <Route path="/inserisci" element={<ArticleData/>}/>
              <Route path="/modifica/:id" element={<ArticleData/>}/>
            </Route>
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
