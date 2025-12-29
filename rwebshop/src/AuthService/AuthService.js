import axios from "axios";

class AuthService{
    jwt="";


    login(username, password){
        axios.post("http://localhost:8090/api/v1.0/utenti/login",{
            "user": username,
            "password": password
        }).then(response => this.setupJwt(response.data.token));
    }

    setupJwt(jwt){
        this.setJwt(jwt);
        axios.interceptors.request.use((config) => {
            config.headers.Authorization = "Bearer "+jwt;
            return config;
        });
    }

    setJwt(jwt){
        localStorage.setItem("token", jwt);
        this.jwt=jwt;
    }

    getJwt(){
        return this.jwt;
    }
}


export default new AuthService();