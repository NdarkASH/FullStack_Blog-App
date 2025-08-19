import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiService } from '../services/apiService';
import { useAuth } from '../components/AuthContext';
import { useMountFade } from '../utils/MountFade';

const RegisterPage = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const mounted = useMountFade(10);

  const navigate = useNavigate();
  const { login } = useAuth();

  const extractErrorMessage = (err: any) => {
    if (!err) return 'Something went wrong';
    // handle ApiError shape
    if (err.message) return err.message;
    if (err.msg) return err.msg;
    if (err.errors && Array.isArray(err.errors)) {
      return err.errors.map((e: any) => e.message).join('; ');
    }
    return String(err);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // basic client validation
    if (!firstName.trim() || !lastName.trim()) {
      setError('First name and last name are required.');
      return;
    }
    if (!email.trim()) {
      setError('Email is required.');
      return;
    }
    if (!password) {
      setError('Password is required.');
      return;
    }
    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    // optional: enforce min length
    if (password.length < 6) {
      setError('Password must be at least 6 characters.');
      return;
    }

    setIsLoading(true);

    try {
      const payload = {
        firstName: firstName.trim(),
        lastName: lastName.trim(),
        email: email.trim(),
        password,
      };

      const result = await apiService.createUser(payload);
      // result shape per your apiService: CreateUser { auth: AuthResponse, user: AuthorResponse }
      if (result?.auth?.token) {
        // set auth in context (and apiService already stored token in localStorage)
        login(result.auth.token);
      }
      navigate('/');
    } catch (err: any) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div
      className={`max-w-md w-full space-y-8 transform transition-all duration-500 ease-out ${mounted ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'}`}
      >
        <div className="max-w-md w-full space-y-8">
            <div>
                <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    Create a new Account
                </h2>
            </div>

            <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
             <div className="rounded-md shadow-sm space-y-4">
                <div className="grid grid-cols-2 gap-4">
                 <div>
                    <label htmlFor="firstName" className="sr-only">
                    First name
                    </label>
                    <input
                    id="firstName"
                    name="firstName"
                    type="text"
                    autoComplete="given-name"
                    required
                    className="appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    placeholder="First name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    disabled={isLoading}
                    />
                 </div>

                <div>
                    <label htmlFor="lastName" className="sr-only">
                    Last name
                    </label>
                    <input
                    id="lastName"
                    name="lastName"
                    type="text"
                    autoComplete="family-name"
                    required
                    className="appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    placeholder="Last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    disabled={isLoading}
                    />
                </div>
                </div>

                <div>
                <label htmlFor="email" className="sr-only">
                    Email address
                </label>
                <input
                    id="email"
                    name="email"
                    type="email"
                    autoComplete="email"
                    required
                    className="appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    placeholder="Email address"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    disabled={isLoading}
                />
                </div>

                <div className="grid grid-cols-2 gap-4">
                <div>
                    <label htmlFor="password" className="sr-only">
                    Password
                    </label>
                    <input
                    id="password"
                    name="password"
                    type="password"
                    autoComplete="new-password"
                    required
                    className="appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    disabled={isLoading}
                    />
                </div>

                <div>
                    <label htmlFor="confirmPassword" className="sr-only">
                    Confirm password
                    </label>
                    <input
                    id="confirmPassword"
                    name="confirmPassword"
                    type="password"
                    autoComplete="new-password"
                    required
                    className="appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    placeholder="Confirm password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    disabled={isLoading}
                    />
                </div>
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

            <div className="space-y-3">
                <div>
                <button
                    type="submit"
                    disabled={isLoading}
                    className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    {isLoading ? 'Registering...' : 'Register'}
                </button>
                </div>

                <div>
                <button
                    type="button"
                    onClick={() => navigate('/login')}
                    disabled={isLoading}
                    className="group relative w-full flex justify-center py-2 px-4 border border-gray-300 text-sm font-medium rounded-md text-indigo-600 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                    Already have an account? Sign in
                </button>
                </div>
            </div>
         </form>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
