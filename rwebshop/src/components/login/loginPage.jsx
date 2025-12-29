import React, { useState } from "react";
import { Formik, Form, Field } from "formik";
import AuthService from "../../API/AuthService/AuthService";
import { Navigate } from "react-router-dom"


const LoginPage = () =>{
    const [error, setError] = useState(null);

    function submitForm(values, { resetForm }){
        AuthService.login(values.user, values.psw, setError);
        
        resetForm();

    }
    if(error == false){
        return <Navigate to="/home" replace/>
    }

    return (
        <div className="row mt-3">
            <div className="col-11">
                <Formik onSubmit={submitForm} initialValues={{user:"", psw:""}} enableReinitialize={true}>
                    <Form>
                        <Field type="text" placeholder="Username" name="user" className="form-control"/><br />
                        <Field type="password" placeholder="Password" name="psw" className="form-control"/><br /><br />
                        <Field type="submit" value="Login" className="btn btn-outline-primary"/>
                    </Form>
                </Formik>
            </div>
        </div>
    );
}

export default LoginPage;