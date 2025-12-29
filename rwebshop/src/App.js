import './App.css';
import Page from "./Page"
import LoginPage from "./components/login/loginPage";
import ArticleData from "./components/articlePage/articleData"
import { BrowserRouter, Routes, Route } from "react-router-dom";
import PrivateAuth from "./components/PrivateAuth/PrivateAuth";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <div className="container d-flex justify-content-center">
          <Routes>
            <Route path="/" exact element={<LoginPage />}/>

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
