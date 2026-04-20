import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [formData, setFormData] = useState({
    nom: '',
    description: '',
    prix: '',
    stock: '',
    imageUrl: '',
    isActive: true
  });

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch('http://localhost:8081/api/products');
      const data = await response.json();
      setProducts(data);
    } catch (error) {
      console.error('Error fetching products:', error);
      toast.error('Erreur lors du chargement des produits');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      nom: '',
      description: '',
      prix: '',
      stock: '',
      imageUrl: '',
      isActive: true
    });
    setEditingProduct(null);
  };

  const handleEdit = (product) => {
    setEditingProduct(product);
    setFormData({
      nom: product.nom,
      description: product.description,
      prix: product.prix,
      stock: product.stock,
      imageUrl: product.imageUrl || '',
      isActive: product.isActive
    });
    setShowModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const url = editingProduct 
        ? `http://localhost:8081/api/products/${editingProduct.id}`
        : 'http://localhost:8081/api/products';
      
      const method = editingProduct ? 'PUT' : 'POST';
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...formData,
          prix: parseFloat(formData.prix),
          stock: parseInt(formData.stock)
        })
      });

      if (response.ok) {
        toast.success(editingProduct ? 'Produit mis à jour' : 'Produit créé');
        setShowModal(false);
        resetForm();
        fetchProducts();
      } else {
        throw new Error('Erreur lors de l\'opération');
      }
    } catch (error) {
      console.error('Error saving product:', error);
      toast.error('Erreur lors de la sauvegarde');
    }
  };

  const handleDelete = async (productId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce produit?')) {
      try {
        await fetch(`http://localhost:8081/api/products/${productId}`, {
          method: 'DELETE'
        });
        toast.success('Produit supprimé');
        fetchProducts();
      } catch (error) {
        console.error('Error deleting product:', error);
        toast.error('Erreur lors de la suppression');
      }
    }
  };

  const toggleProductStatus = async (productId, isActive) => {
    try {
      await fetch(`http://localhost:8081/api/products/${productId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ isActive: !isActive })
      });
      toast.success(`Produit ${!isActive ? 'activé' : 'désactivé'}`);
      fetchProducts();
    } catch (error) {
      console.error('Error toggling product status:', error);
      toast.error('Erreur lors du changement de statut');
    }
  };

  const updateStock = async (productId, quantity) => {
    try {
      await fetch(`http://localhost:8081/api/products/${productId}/increase-stock?quantity=${quantity}`, {
        method: 'PUT'
      });
      toast.success(`Stock augmenté de ${quantity}`);
      fetchProducts();
    } catch (error) {
      console.error('Error updating stock:', error);
      toast.error('Erreur lors de la mise à jour du stock');
    }
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
          <i className="bi bi-box"></i> Gestion des Produits
        </h2>
        <button 
          className="btn btn-primary"
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
        >
          <i className="bi bi-plus-circle"></i> Ajouter un produit
        </button>
      </div>

      <div className="card">
        <div className="card-body">
          <div className="table-responsive">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Image</th>
                  <th>Nom</th>
                  <th>Description</th>
                  <th>Prix</th>
                  <th>Stock</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {products.map(product => (
                  <tr key={product.id}>
                    <td>{product.id}</td>
                    <td>
                      {product.imageUrl ? (
                        <img 
                          src={product.imageUrl} 
                          alt={product.nom}
                          style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                        />
                      ) : (
                        <div className="bg-light d-flex align-items-center justify-content-center" style={{ width: '50px', height: '50px' }}>
                          <i className="bi bi-box text-muted"></i>
                        </div>
                      )}
                    </td>
                    <td>{product.nom}</td>
                    <td>
                      <div style={{ maxWidth: '200px', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                        {product.description}
                      </div>
                    </td>
                    <td>{product.prix.toFixed(2)} MAD</td>
                    <td>
                      <span className={`badge ${product.stock > 10 ? 'bg-success' : 'bg-warning'}`}>
                        {product.stock}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${product.isActive ? 'bg-success' : 'bg-secondary'}`}>
                        {product.isActive ? 'Actif' : 'Inactif'}
                      </span>
                    </td>
                    <td>
                      <div className="btn-group btn-group-sm">
                        <button 
                          className="btn btn-outline-primary"
                          onClick={() => handleEdit(product)}
                        >
                          <i className="bi bi-pencil"></i>
                        </button>
                        <button 
                          className={`btn ${product.isActive ? 'btn-outline-warning' : 'btn-outline-success'}`}
                          onClick={() => toggleProductStatus(product.id, product.isActive)}
                        >
                          <i className={`bi ${product.isActive ? 'bi-pause' : 'bi-play'}`}></i>
                        </button>
                        <button 
                          className="btn btn-outline-info"
                          onClick={() => {
                            const quantity = prompt('Quantité à ajouter:', '10');
                            if (quantity && !isNaN(quantity)) {
                              updateStock(product.id, parseInt(quantity));
                            }
                          }}
                        >
                          <i className="bi bi-plus"></i>
                        </button>
                        <button 
                          className="btn btn-outline-danger"
                          onClick={() => handleDelete(product.id)}
                        >
                          <i className="bi bi-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="modal show" style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  {editingProduct ? 'Modifier le produit' : 'Ajouter un produit'}
                </h5>
                <button 
                  type="button" 
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>
              <form onSubmit={handleSubmit}>
                <div className="modal-body">
                  <div className="row">
                    <div className="col-md-6">
                      <div className="mb-3">
                        <label className="form-label">Nom du produit</label>
                        <input
                          type="text"
                          className="form-control"
                          value={formData.nom}
                          onChange={(e) => setFormData({...formData, nom: e.target.value})}
                          required
                        />
                      </div>
                    </div>
                    <div className="col-md-6">
                      <div className="mb-3">
                        <label className="form-label">Prix (MAD)</label>
                        <input
                          type="number"
                          step="0.01"
                          min="0"
                          className="form-control"
                          value={formData.prix}
                          onChange={(e) => setFormData({...formData, prix: e.target.value})}
                          required
                        />
                      </div>
                    </div>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Description</label>
                    <textarea
                      className="form-control"
                      value={formData.description}
                      onChange={(e) => setFormData({...formData, description: e.target.value})}
                      rows="3"
                      required
                    ></textarea>
                  </div>
                  <div className="row">
                    <div className="col-md-6">
                      <div className="mb-3">
                        <label className="form-label">Stock</label>
                        <input
                          type="number"
                          min="0"
                          className="form-control"
                          value={formData.stock}
                          onChange={(e) => setFormData({...formData, stock: e.target.value})}
                          required
                        />
                      </div>
                    </div>
                    <div className="col-md-6">
                      <div className="mb-3">
                        <label className="form-label">URL de l'image</label>
                        <input
                          type="url"
                          className="form-control"
                          value={formData.imageUrl}
                          onChange={(e) => setFormData({...formData, imageUrl: e.target.value})}
                          placeholder="https://example.com/image.jpg"
                        />
                      </div>
                    </div>
                  </div>
                  <div className="mb-3">
                    <div className="form-check">
                      <input
                        type="checkbox"
                        className="form-check-input"
                        id="isActive"
                        checked={formData.isActive}
                        onChange={(e) => setFormData({...formData, isActive: e.target.checked})}
                      />
                      <label className="form-check-label" htmlFor="isActive">
                        Produit actif
                      </label>
                    </div>
                  </div>
                </div>
                <div className="modal-footer">
                  <button 
                    type="button" 
                    className="btn btn-secondary"
                    onClick={() => setShowModal(false)}
                  >
                    Annuler
                  </button>
                  <button type="submit" className="btn btn-primary">
                    {editingProduct ? 'Mettre à jour' : 'Créer'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductManagement;
