import React from "react"
import { createContext, useState } from "react"
import { Navigate } from "react-router-dom"

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [tokens, setTokens] = useState({
        "access_token": "",
        "refresh_token": ""
    });

    return (
        <AuthContext.Provider value={ {tokens, setTokens} }>
            {children}
        </AuthContext.Provider>
    );
}