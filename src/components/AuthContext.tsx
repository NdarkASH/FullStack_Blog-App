import React, {createContext, useContext, useState, useCallback, useEffect} from "react";

import { apiService } from "@/services/apiService";
import { UUID } from "crypto";


interface AuthUser {
    id: UUID;
    name: string;
    email: string;
}

interface AuthContextType {
    isAuthenticated: boolean;
    user: AuthUser | null;
    login: (token: string) => Promise<void>;
    logout: () => void;
    token: string | null;
}

interface ApiWrapperResponse<T> {
    code: number;
    msg: string;
    data: T;
}


interface AuthProviderProps {
    children: React.ReactNode;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider: React.FC<AuthProviderProps> = ({
    children
}) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [user, setUser] = useState<AuthUser | null>(null);
    const [token, setToken] = useState<string | null>
    (localStorage.getItem('token'))


    useEffect(() => {
        const initializeAuth = async () => {
            const storedToken = localStorage.getItem('token');

            if (storedToken) {
                try {
                    setIsAuthenticated(true);
                    setToken(storedToken);
                } catch (error) {
                    localStorage.removeItem('token');
                    setIsAuthenticated(false);
                    setUser(null);
                    setToken(null);
                }
            }
        };

        initializeAuth();
    }, []);

    const login = useCallback(async(token: string) => {
        try {
            // const response = await apiService.login({
            //     email, password
            // });

            localStorage.setItem('token', token);
            setToken(token);
            setIsAuthenticated(true);

        }catch(error){
            throw error;
        }
    }, []);

    const logout = useCallback(() => {
        localStorage.removeItem('token');
        setIsAuthenticated(false);
        setUser(null);
        setToken(null);
        apiService.logout;
    }, []);

    useEffect(() => {
        if (token) {
            const axiosInstance = apiService['api'];

            axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`
        }
    }, [token]);

    const value = {
        isAuthenticated,
        user,
        login,
        logout,
        token
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );

}

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);

    if (!context) {

        throw new Error('useAuth must be used within an AuthProvider');

    }
    
    return context;
};

export default AuthContext;





