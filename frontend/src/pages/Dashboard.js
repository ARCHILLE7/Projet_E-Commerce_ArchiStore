import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { toast } from 'react-toastify';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalProducts: 0,
    totalOrders: 0,
    totalRevenue: 0
  });
  const [recentOrders, setRecentOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    if (user && user.role === 'ADMIN') {
      fetchDashboardData();
    }
  }, [user]);

  const fetchDashboardData = async () => {
    try {
      // Fetch statistics
      const [usersRes, productsRes, ordersRes] = await Promise.all([
        fetch('http://localhost:8082/api/users'),
        fetch('http://localhost:8081/api/products'),
        fetch('http://localhost:8083/api/orders')
      ]);

      const users = await usersRes.json();
      const products = await productsRes.json();
      const orders = await ordersRes.json();

      const totalRevenue = orders.reduce((sum, order) => sum + order.total, 0);

      setStats({
        totalUsers: users.length,
        totalProducts: products.length,
        totalOrders: orders.length,
        totalRevenue
      });

      // Get recent orders (last 5)
      const sortedOrders = orders
        .sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
        .slice(0, 5);
      setRecentOrders(sortedOrders);

    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      toast.error('Erreur lors du chargement des données du dashboard');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusBadge = (status) => {
    const statusClasses = {
      'PENDING': 'badge-warning',
      'CONFIRMED': 'badge-info',
      'SHIPPED': 'badge-primary',
      'DELIVERED': 'badge-success',
      'CANCELLED': 'badge-danger',
      'REFUNDED': 'badge-secondary'
    };
    
    const statusLabels = {
      'PENDING': 'En attente',
      'CONFIRMED': 'Confirmée',
      'SHIPPED': 'Expédiée',
      'DELIVERED': 'Livrée',
      'CANCELLED': 'Annulée',
      'REFUNDED': 'Remboursée'
    };

    return (
      <span className={`badge ${statusClasses[status] || 'badge-secondary'}`}>
        {statusLabels[status] || status}
      </span>
    );
  };

  if (loading) {
    return (
      <div className="loading-spinner">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Chargement...</span>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h2 className="mb-4">
        <i className="bi bi-speedometer2"></i> Dashboard Administrateur
      </h2>

      {/* Statistics Cards */}
      <div className="dashboard-stats">
        <div className="row">
          <div className="col-md-3">
            <div className="text-center">
              <h3 className="display-4">{stats.totalUsers}</h3>
              <p className="mb-0">
                <i className="bi bi-people"></i> Utilisateurs
              </p>
            </div>
          </div>
          <div className="col-md-3">
            <div className="text-center">
              <h3 className="display-4">{stats.totalProducts}</h3>
              <p className="mb-0">
                <i className="bi bi-box"></i> Produits
              </p>
            </div>
          </div>
          <div className="col-md-3">
            <div className="text-center">
              <h3 className="display-4">{stats.totalOrders}</h3>
              <p className="mb-0">
                <i className="bi bi-receipt"></i> Commandes
              </p>
            </div>
          </div>
          <div className="col-md-3">
            <div className="text-center">
              <h3 className="display-4">{stats.totalRevenue.toFixed(0)}</h3>
              <p className="mb-0">
                <i className="bi bi-currency-dollar"></i> Revenu (MAD)
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="row mt-4">
        {/* Recent Orders */}
        <div className="col-md-8">
          <div className="card">
            <div className="card-header">
              <h5 className="mb-0">
                <i className="bi bi-clock-history"></i> Commandes récentes
              </h5>
            </div>
            <div className="card-body">
              {recentOrders.length === 0 ? (
                <p className="text-muted">Aucune commande récente</p>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th># Commande</th>
                        <th>Date</th>
                        <th>Client</th>
                        <th>Total</th>
                        <th>Statut</th>
                      </tr>
                    </thead>
                    <tbody>
                      {recentOrders.map(order => (
                        <tr key={order.id}>
                          <td>#{order.id}</td>
                          <td>{formatDate(order.orderDate)}</td>
                          <td>Client #{order.utilisateurId}</td>
                          <td>{order.total.toFixed(2)} MAD</td>
                          <td>{getStatusBadge(order.status)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="col-md-4">
          <div className="card">
            <div className="card-header">
              <h5 className="mb-0">
                <i className="bi bi-lightning"></i> Actions rapides
              </h5>
            </div>
            <div className="card-body">
              <div className="d-grid gap-2">
                <a href="/admin/users" className="btn btn-outline-primary">
                  <i className="bi bi-people"></i> Gérer les utilisateurs
                </a>
                <a href="/admin/products" className="btn btn-outline-success">
                  <i className="bi bi-box"></i> Gérer les produits
                </a>
                <a href="/admin/orders" className="btn btn-outline-info">
                  <i className="bi bi-receipt"></i> Gérer les commandes
                </a>
                <button className="btn btn-outline-warning">
                  <i className="bi bi-graph-up"></i> Voir les statistiques
                </button>
              </div>
            </div>
          </div>

          {/* System Info */}
          <div className="card mt-3">
            <div className="card-header">
              <h5 className="mb-0">
                <i className="bi bi-info-circle"></i> Informations système
              </h5>
            </div>
            <div className="card-body">
              <h6>Services actifs</h6>
              <div className="list-group list-group-flush">
                <div className="list-group-item d-flex justify-content-between align-items-center">
                  <span><i className="bi bi-shield-check"></i> Security Service</span>
                  <span className="badge bg-success">Actif</span>
                </div>
                <div className="list-group-item d-flex justify-content-between align-items-center">
                  <span><i className="bi bi-people"></i> User Service</span>
                  <span className="badge bg-success">Actif</span>
                </div>
                <div className="list-group-item d-flex justify-content-between align-items-center">
                  <span><i className="bi bi-box"></i> Product Service</span>
                  <span className="badge bg-success">Actif</span>
                </div>
                <div className="list-group-item d-flex justify-content-between align-items-center">
                  <span><i className="bi bi-receipt"></i> Order Service</span>
                  <span className="badge bg-success">Actif</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
