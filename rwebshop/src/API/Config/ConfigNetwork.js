import axios from "axios";
let tokens=null;

const api=axios.create({withCredentials: true});

/*api.interceptors.request.use(
    config => {
        if (tokens?.access_token) {
            config.headers['Authorization'] = `Bearer ${tokens.access_token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);*/

api.interceptors.response.use(
    response => {
        //console.log(response);
        return response;
    },
    async error => {
        //console.log(error);
        const originalRequest = error.config;

        //evita loop
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                //refresh token
                await api.post(
                    "http://localhost:8090/api/v1.0/utenti/refresh",
                    {},
                    { withCredentials: true }
                );

                //riprovo richiesta
                return api(originalRequest);

            } catch (refreshError) {
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export function setTokens(t){
    if(t["access_token"]){
        tokens=t;
    }
}

export default api;