import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider, useAuth } from "./components/AuthContext";
import "../src/App.css"
import NavBar from "./components/Navbar";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useAuth();

  if(!isAuthenticated) {
    return <Navigate to="/login" replace/>
  }

  return <>{children}</>
};

function AppContent() {
  const { isAuthenticated, logout, user} = useAuth();
  return (
    <BrowserRouter>
      <NavBar
      isAuthenticated={isAuthenticated}
      userProfile={user ? {
        name: user.name,
        avatar: undefined
      } : undefined}
      onLogout={logout}
      />
      <main className="container mx-auto py-6">
        <Routes>
          <Route path="/" element={<LoginPage />}/>
          <Route path="/login" element={<HomePage />}/>
        </Routes>
        

      </main>
    </BrowserRouter>
  )
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
