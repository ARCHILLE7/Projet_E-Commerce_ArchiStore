import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navbar = () => {
  const { user, logout, hasRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">
        <Link className="navbar-brand" to="/">
          <i className="bi bi-shop"></i> E-Commerce ArchiStore
        </Link>
        
        <button 
          className="navbar-toggler" 
          type="button" 
          data-bs-toggle="collapse" 
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <Link className="nav-link" to="/">
                <i className="bi bi-house"></i> Accueil
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/products">
                <i className="bi bi-box"></i> Produits
              </Link>
            </li>
            {user && (
              <li className="nav-item">
                <Link className="nav-link" to="/orders">
                  <i className="bi bi-receipt"></i> Mes Commandes
                </Link>
              </li>
            )}
            {hasRole('ADMIN') && (
              <>
                <li className="nav-item dropdown">
                  <button 
                    className="nav-link dropdown-toggle" 
                    type="button" 
                    data-bs-toggle="dropdown"
                  >
                    <i className="bi bi-gear"></i> Administration
                  </button>
                  <ul className="dropdown-menu">
                    <li>
                      <Link className="dropdown-item" to="/dashboard">
                        <i className="bi bi-speedometer2"></i> Dashboard
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/admin/users">
                        <i className="bi bi-people"></i> Utilisateurs
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/admin/products">
                        <i className="bi bi-box"></i> Produits
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/admin/orders">
                        <i className="bi bi-receipt"></i> Commandes
                      </Link>
                    </li>
                  </ul>
                </li>
              </>
            )}
          </ul>
          
          <ul className="navbar-nav">
            {user ? (
              <>
                <li className="nav-item dropdown">
                  <button 
                    className="nav-link dropdown-toggle" 
                    type="button" 
                    data-bs-toggle="dropdown"
                  >
                    <i className="bi bi-person-circle"></i> {user.email}
                    <span className="badge bg-primary ms-2">{user.role}</span>
                  </button>
                  <ul className="dropdown-menu">
                    <li>
                      <button className="dropdown-item" onClick={handleLogout}>
                        <i className="bi bi-box-arrow-right"></i> Déconnexion
                      </button>
                    </li>
                  </ul>
                </li>
              </>
            ) : (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">
                    <i className="bi bi-box-arrow-in-right"></i> Connexion
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">
                    <i className="bi bi-person-plus"></i> Inscription
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
