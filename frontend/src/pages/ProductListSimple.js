import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';

const ProductListSimple = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [cart, setCart] = useState([]);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      console.log('🔄 Début chargement produits...');
      console.log('📍 URL API: http://localhost:8085/api/products');
      
      const response = await fetch('http://localhost:8085/api/products');
      
      console.log('📊 Status response:', response.status);
      console.log('📊 Headers response:', response.headers);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      const data = await response.json();
      console.log('✅ Produits reçus:', data);
      console.log('📦 Nombre de produits:', data.length);
      
      setProducts(data);
      toast.success(`${data.length} produits chargés!`);
    } catch (error) {
      console.error('❌ Erreur détaillée:', error);
      console.error('❌ Message:', error.message);
      console.error('❌ Stack:', error.stack);
      toast.error(`Erreur: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const filteredProducts = products.filter(product =>
    product.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const addToCart = async (product) => {
    try {
      console.log('🛒 Ajout au panier:', product.nom);
      
      const response = await fetch('http://localhost:8082/api/simple-cart/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: 19,
          productId: product.id,
          productName: product.nom,
          price: product.prix,
          quantity: 1
        })
      });
      
      const result = await response.json();
      console.log('📝 Résultat ajout panier:', result);
      
      if (result.success) {
        toast.success(`${product.nom} ajouté au panier!`);
        setCart([...cart, product]);
      } else {
        toast.error('Erreur lors de l\'ajout au panier');
      }
    } catch (error) {
      console.error('❌ Erreur ajout panier:', error);
      toast.error(`Erreur: ${error.message}`);
    }
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Chargement...</span>
          </div>
          <p className="mt-3">Chargement des produits...</p>
        </div>
      </div>
    );
  }

  return (
    <div>
      <div className="row mb-4">
        <div className="col-md-8">
          <h2>
            <i className="bi bi-box"></i> Nos Produits ({products.length})
          </h2>
        </div>
        <div className="col-md-4">
          <div className="input-group">
            <span className="input-group-text">
              <i className="bi bi-search"></i>
            </span>
            <input
              type="text"
              className="form-control"
              placeholder="Rechercher un produit..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>
      </div>

      <div className="row">
        {filteredProducts.length === 0 ? (
          <div className="col-12">
            <div className="alert alert-warning">
              <i className="bi bi-info-circle"></i> 
              {products.length === 0 ? 'Aucun produit disponible' : 'Aucun produit trouvé pour votre recherche'}
            </div>
          </div>
        ) : (
          filteredProducts.map(product => (
            <div key={product.id} className="col-md-6 col-lg-4 mb-4">
              <div className="card product-card h-100">
                {product.imageUrl ? (
                  <img 
                    src={product.imageUrl} 
                    className="card-img-top product-image" 
                    alt={product.nom}
                  />
                ) : (
                  <div className="card-img-top product-image d-flex align-items-center justify-content-center bg-light">
                    <i className="bi bi-box display-4 text-muted"></i>
                  </div>
                )}
                <div className="card-body d-flex flex-column">
                  <h5 className="card-title">{product.nom}</h5>
                  <p className="card-text flex-grow-1">
                    {product.description}
                  </p>
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="h5 text-primary mb-0">
                      {product.prix.toFixed(2)} MAD
                    </span>
                    <span className={`badge ${product.stock > 10 ? 'bg-success' : 'bg-warning'}`}>
                      Stock: {product.stock}
                    </span>
                  </div>
                  <button
                    className="btn btn-primary w-100"
                    onClick={() => addToCart(product)}
                    disabled={product.stock === 0}
                  >
                    <i className="bi bi-cart-plus"></i>
                    {product.stock === 0 ? 'Indisponible' : 'Ajouter au panier'}
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {cart.length > 0 && (
        <div className="position-fixed bottom-0 end-0 p-3">
          <div className="card">
            <div className="card-body">
              <h6 className="card-title">
                <i className="bi bi-cart"></i> Panier ({cart.length})
              </h6>
              <button className="btn btn-success btn-sm">
                Passer commande
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductListSimple;
