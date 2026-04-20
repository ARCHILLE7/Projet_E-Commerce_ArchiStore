import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';

const AuthContext = createContext();
const AUTH_SERVICE_URLS = [process.env.REACT_APP_AUTH_SERVICE_URL, 'http://localhost:8090', 'http://localhost:8082'].filter(Boolean);
const USER_SERVICE_URLS = [process.env.REACT_APP_USER_SERVICE_URL, 'http://localhost:8082'].filter(Boolean);

const uniqueUrls = (urls) => [...new Set(urls)];

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      fetchCurrentUser();
    } else {
      setLoading(false);
    }
  }, []);

  const fetchCurrentUser = async () => {
    const currentUserEndpoint = `${USER_SERVICE_URLS[0]}/api/users/me`;
    try {
      const response = await axios.get(currentUserEndpoint);
      setUser(response.data);
    } catch (error) {
      console.error('Error fetching current user:', error);
      localStorage.removeItem('token');
      delete axios.defaults.headers.common['Authorization'];
    } finally {
      setLoading(false);
    }
  };

  const login = async (credentials) => {
    const loginEndpoints = uniqueUrls([
      ...AUTH_SERVICE_URLS.map((baseUrl) => `${baseUrl}/api/auth/login`),
      ...USER_SERVICE_URLS.map((baseUrl) => `${baseUrl}/api/auth/simple-login`)
    ]);

    let lastError = null;

    for (const endpoint of loginEndpoints) {
      try {
        const response = await axios.post(endpoint, credentials);
        const data = response.data || {};

        if (data.success === false) {
          throw new Error(data.message || 'Email ou mot de passe incorrect');
        }

        const token = data.token;
        const userId = data.userId || data.id;
        const email = data.email || credentials.email;
        const role = data.role || 'USER';

        if (!token) {
          throw new Error('Réponse de connexion invalide (token manquant)');
        }

        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify({ id: userId, email, role }));
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        setUser({ id: userId, email, role });
        toast.success('Connexion réussie!');
        return true;
      } catch (error) {
        lastError = error;
      }
    }

    console.error('Login error:', lastError);
    toast.error('Erreur lors de la connexion: ' + (lastError?.response?.data?.message || lastError?.message || 'Email ou mot de passe incorrect'));
    return false;
  };

  const register = async (userData) => {
    const registerAttempts = [
      { endpoint: `${USER_SERVICE_URLS[0]}/api/users/register`, payload: userData },
      { endpoint: `${USER_SERVICE_URLS[0]}/api/auth/register`, payload: { email: userData.email, password: userData.password } },
      { endpoint: `${AUTH_SERVICE_URLS[0]}/api/auth/register`, payload: { email: userData.email, password: userData.password } }
    ];

    let lastError = null;

    for (const attempt of registerAttempts) {
      if (!attempt.endpoint.startsWith('http')) {
        continue;
      }

      try {
        await axios.post(attempt.endpoint, attempt.payload);
        toast.success('Inscription réussie! Vous pouvez maintenant vous connecter.');
        return true;
      } catch (error) {
        lastError = error;
      }
    }

    console.error('Registration error:', lastError);
    toast.error('Erreur lors de l\'inscription: ' + (lastError?.response?.data?.message || lastError?.message || 'Veuillez vérifier vos informations'));
    return false;
  };

  const logout = () => {
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];
    setUser(null);
    toast.info('Vous avez été déconnecté');
  };

  const hasRole = (role) => {
    return user && user.role === role;
  };

  const hasAnyRole = (roles) => {
    return user && roles.includes(user.role);
  };

  const value = {
    user,
    login,
    register,
    logout,
    hasRole,
    hasAnyRole,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
