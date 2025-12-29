import axios from "axios";

class AuthService{

    login(username, password, errManager){
        axios.post("http://localhost:8090/api/v1.0/utenti/login",{
            "user": username,
            "password": password
        }, {            
            withCredentials: true
        }).then(resp => {
            errManager(false);
        })
        .catch(err => errManager(true));
    }
}


export default new AuthService();