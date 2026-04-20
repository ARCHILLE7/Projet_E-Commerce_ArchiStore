import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { toast } from 'react-toastify';

const OrderList = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      fetchOrders();
    }
  }, [user]);

  const fetchOrders = async () => {
    try {
      const response = await fetch(`http://localhost:8083/api/orders/user/${user.id}`);
      const data = await response.json();
      setOrders(data);
    } catch (error) {
      console.error('Error fetching orders:', error);
      toast.error('Erreur lors du chargement des commandes');
    } finally {
      setLoading(false);
    }
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

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
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
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>
          <i className="bi bi-receipt"></i> Mes Commandes
        </h2>
        <span className="badge bg-primary">
          {orders.length} commande{orders.length > 1 ? 's' : ''}
        </span>
      </div>

      {orders.length === 0 ? (
        <div className="alert alert-info">
          <i className="bi bi-info-circle"></i> Vous n'avez pas encore de commande.
          <br />
          <a href="/products" className="alert-link">Découvrez nos produits</a>
        </div>
      ) : (
        <div className="row">
          {orders.map(order => (
            <div key={order.id} className="col-12 mb-4">
              <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                  <div>
                    <h6 className="mb-0">Commande #{order.id}</h6>
                    <small className="text-muted">{formatDate(order.orderDate)}</small>
                  </div>
                  <div className="d-flex align-items-center">
                    {getStatusBadge(order.status)}
                    <span className="ms-3 h5 mb-0 text-primary">
                      {order.total.toFixed(2)} MAD
                    </span>
                  </div>
                </div>
                <div className="card-body">
                  <div className="row">
                    <div className="col-md-6">
                      <h6>Adresse de livraison</h6>
                      <p className="mb-0">{order.shippingAddress}</p>
                    </div>
                    <div className="col-md-6">
                      <h6>Informations</h6>
                      <p className="mb-0">
                        <strong>N° de suivi:</strong> {order.trackingNumber || 'Non disponible'}
                      </p>
                      <p className="mb-0">
                        <strong>Livraison estimée:</strong> {order.estimatedDelivery ? 
                          formatDate(order.estimatedDelivery) : 'Non spécifiée'}
                      </p>
                    </div>
                  </div>
                  
                  {order.orderProducts && order.orderProducts.length > 0 && (
                    <div className="mt-3">
                      <h6>Produits commandés</h6>
                      <div className="table-responsive">
                        <table className="table table-sm">
                          <thead>
                            <tr>
                              <th>Produit</th>
                              <th>Quantité</th>
                              <th>Prix unitaire</th>
                              <th>Total</th>
                            </tr>
                          </thead>
                          <tbody>
                            {order.orderProducts.map((item, index) => (
                              <tr key={index}>
                                <td>{item.productName || `Produit #${item.produitId}`}</td>
                                <td>{item.quantite}</td>
                                <td>{item.unitPrice.toFixed(2)} MAD</td>
                                <td>{item.totalPrice.toFixed(2)} MAD</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    </div>
                  )}
                </div>
                <div className="card-footer">
                  <div className="d-flex justify-content-end">
                    {order.status === 'PENDING' && (
                      <button className="btn btn-danger btn-sm me-2">
                        <i className="bi bi-x-circle"></i> Annuler
                      </button>
                    )}
                    {order.status === 'DELIVERED' && (
                      <button className="btn btn-success btn-sm">
                        <i className="bi bi-star"></i> Noter
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default OrderList;
