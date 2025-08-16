import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { apiService  } from "@/services/apiService";
import { useAuth } from "@/components/AuthContext";


const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await apiService.login({ email, password});

            login(response.token);
            navigate('/');
        } catch (err: any) {
            setError(err.message || 'Failed to login. Please try again');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex item-center justify-center bg-gray-50">
            <div className="max-w-md w-full space-y-8">
                <div>
                    <h2 className="mt-6 text-center text-3xl font-extrabold textgray-900">
                        Sing in to your account
                    </h2>
                </div>

                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    <div className="rounded-md shadow-sm space-y-4">
                        <div>
                            <label className="sr-only" htmlFor="email">
                                Email address
                            </label>
                            <input 
                            id="email" 
                            name="email"
                            type="email"
                            autoComplete="email"
                            required
                            className="appearance-none"
                            />
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
}