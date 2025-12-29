import axios from "axios";
const api=axios.create({
    withCredentials: true
});

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

export default api;