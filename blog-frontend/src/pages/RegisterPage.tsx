import { useAuth } from "@/components/AuthContext";
import { apiService } from "@/service/apiService";
import { useState } from "react"
import { useNavigate } from "react-router-dom";

const RegisterPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const navigate = useNavigate();
    const { login } = useAuth();


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);
        try {
            const response = await apiService.register({
                email,
                password,
                firstName,
                lastName,
            });
    
            if (response?.token) {
                login(response.token)
            }
            navigate('/');
    
        } catch (err: any) {
            setError("Failed to register. Please try again")
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50">
            <div>
                <div>
                    <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
                        Create a new Account
                    </h2>
                </div>
                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    <div className="rounded-md shadow-sm space-y-4">
                        <div>
                            <label htmlFor="firstName" className="sr-only">First Name</label>
                        <input
                        id="firstName"
                        name="firstName" 
                        type="text"
                        required
                        className="appearance-none rounded relative block w-full px-3 placeholder-gray-500 text-gray-900 focus:outline-non focus:ring-indigo-500 focus:z-10 sm:text-sm"
                        placeholder="FirstName"
                        value={firstName}
                        onChange={((e)=> setFirstName(e.target.value))}
                        disabled={isLoading}
                        />
                        </div>
                        <div>
                            <label htmlFor="lastName" className="sr-only">First Name</label>
                        <input
                        id="lastName"
                        name="lastName" 
                        type="text"
                        required
                        className="appearance-none rounded relative block w-full px-3 placeholder-gray-500 text-gray-900 focus:outline-non focus:ring-indigo-500 focus:z-10 sm:text-sm"
                        placeholder="lastName"
                        value={lastName}
                        onChange={((e)=> setLastName(e.target.value))}
                        disabled={isLoading}
                        />
                        </div>
                        <div>
                            <label htmlFor="email" className="sr-only">First Name</label>
                        <input
                        id="email"
                        name="email" 
                        type="text"
                        required
                        className="appearance-none rounded relative block w-full px-3 placeholder-gray-500 text-gray-900 focus:outline-non focus:ring-indigo-500 focus:z-10 sm:text-sm"
                        placeholder="email"
                        value={email}
                        onChange={((e)=> setEmail(e.target.value))}
                        disabled={isLoading}
                        />
                        </div>
                        <div>
                            <label htmlFor="password" className="sr-only">First Name</label>
                        <input
                        id="password"
                        name="password" 
                        type="text"
                        required
                        className="appearance-none rounded relative block w-full px-3 placeholder-gray-500 text-gray-900 focus:outline-non focus:ring-indigo-500 focus:z-10 sm:text-sm"
                        placeholder="password"
                        value={password}
                        onChange={((e)=> setPassword(e.target.value))}
                        disabled={isLoading}
                        />
                        </div>
                    </div>
                    {error && (
                        <div className="rounded-md bg-red-50 p-4">
                            <div className="flex">
                                <div className="ml-3">
                                    <h3 className="text-sm font-medium text-red-800">{error}</h3>
                                </div>
                            </div>
                        </div>
                    )}
                    <div>
                        <button
                        type="submit"
                        disabled={isLoading}
                        className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {isLoading ? 'Registering...' : 'Register'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
};

export default RegisterPage;

