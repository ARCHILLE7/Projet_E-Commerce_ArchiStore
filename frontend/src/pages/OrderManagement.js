import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await fetch('http://localhost:8083/api/orders');
      const data = await response.json();
      setOrders(data);
    } catch (error) {
      console.error('Error fetching orders:', error);
      toast.error('Erreur lors du chargement des commandes');
    } finally {
      setLoading(false);
    }
  };

  const updateOrderStatus = async (orderId, newStatus) => {
    try {
      await fetch(`http://localhost:8083/api/orders/${orderId}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status: newStatus })
      });
      toast.success(`Statut de la commande mis à jour: ${newStatus}`);
      fetchOrders();
    } catch (error) {
      console.error('Error updating order status:', error);
      toast.error('Erreur lors de la mise à jour du statut');
    }
  };

  const viewOrderDetails = (order) => {
    setSelectedOrder(order);
    setShowModal(true);
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

  const getStatusActions = (status) => {
    const actions = [];
    
    switch (status) {
      case 'PENDING':
        actions.push(
          { label: 'Confirmer', status: 'CONFIRMED', class: 'btn-success' },
          { label: 'Annuler', status: 'CANCELLED', class: 'btn-danger' }
        );
        break;
      case 'CONFIRMED':
        actions.push(
          { label: 'Expédier', status: 'SHIPPED', class: 'btn-primary' },
          { label: 'Annuler', status: 'CANCELLED', class: 'btn-danger' }
        );
        break;
      case 'SHIPPED':
        actions.push(
          { label: 'Livrer', status: 'DELIVERED', class: 'btn-success' }
        );
        break;
      case 'DELIVERED':
        actions.push(
          { label: 'Rembourser', status: 'REFUNDED', class: 'btn-warning' }
        );
        break;
      default:
        break;
    }
    
    return actions;
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
          <i className="bi bi-receipt"></i> Gestion des Commandes
        </h2>
        <span className="badge bg-primary">
          {orders.length} commande{orders.length > 1 ? 's' : ''}
        </span>
      </div>

      <div className="card">
        <div className="card-body">
          <div className="table-responsive">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th># Commande</th>
                  <th>Date</th>
                  <th>Client</th>
                  <th>Total</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id}>
                    <td>#{order.id}</td>
                    <td>{formatDate(order.orderDate)}</td>
                    <td>Client #{order.utilisateurId}</td>
                    <td>{order.total.toFixed(2)} MAD</td>
                    <td>{getStatusBadge(order.status)}</td>
                    <td>
                      <div className="btn-group btn-group-sm">
                        <button 
                          className="btn btn-outline-info"
                          onClick={() => viewOrderDetails(order)}
                        >
                          <i className="bi bi-eye"></i>
                        </button>
                        {getStatusActions(order.status).map((action, index) => (
                          <button
                            key={index}
                            className={`btn ${action.class}`}
                            onClick={() => updateOrderStatus(order.id, action.status)}
                          >
                            {action.label}
                          </button>
                        ))}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Order Details Modal */}
      {showModal && selectedOrder && (
        <div className="modal show" style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  Détails de la commande #{selectedOrder.id}
                </h5>
                <button 
                  type="button" 
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>
              <div className="modal-body">
                <div className="row mb-3">
                  <div className="col-md-6">
                    <h6>Informations générales</h6>
                    <p><strong>Date:</strong> {formatDate(selectedOrder.orderDate)}</p>
                    <p><strong>Statut:</strong> {getStatusBadge(selectedOrder.status)}</p>
                    <p><strong>Total:</strong> {selectedOrder.total.toFixed(2)} MAD</p>
                  </div>
                  <div className="col-md-6">
                    <h6>Livraison</h6>
                    <p><strong>Adresse:</strong> {selectedOrder.shippingAddress}</p>
                    <p><strong>N° de suivi:</strong> {selectedOrder.trackingNumber || 'Non disponible'}</p>
                    <p><strong>Livraison estimée:</strong> {selectedOrder.estimatedDelivery ? 
                      formatDate(selectedOrder.estimatedDelivery) : 'Non spécifiée'}
                    </p>
                  </div>
                </div>
                
                <h6>Produits commandés</h6>
                {selectedOrder.orderProducts && selectedOrder.orderProducts.length > 0 ? (
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
                        {selectedOrder.orderProducts.map((item, index) => (
                          <tr key={index}>
                            <td>{item.productName || `Produit #${item.produitId}`}</td>
                            <td>{item.quantite}</td>
                            <td>{item.unitPrice.toFixed(2)} MAD</td>
                            <td>{item.totalPrice.toFixed(2)} MAD</td>
                          </tr>
                        ))}
                      </tbody>
                      <tfoot>
                        <tr>
                          <th colSpan="3">Total:</th>
                          <th>{selectedOrder.total.toFixed(2)} MAD</th>
                        </tr>
                      </tfoot>
                    </table>
                  </div>
                ) : (
                  <p className="text-muted">Aucun produit trouvé pour cette commande</p>
                )}
              </div>
              <div className="modal-footer">
                <button 
                  type="button" 
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  Fermer
                </button>
                {getStatusActions(selectedOrder.status).map((action, index) => (
                  <button
                    key={index}
                    className={`btn ${action.class}`}
                    onClick={() => {
                      updateOrderStatus(selectedOrder.id, action.status);
                      setShowModal(false);
                    }}
                  >
                    {action.label}
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default OrderManagement;
