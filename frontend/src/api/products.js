// API pour les produits
const DEFAULT_PRODUCT_URLS = ['http://localhost:8085', 'http://localhost:8081'];

const getCandidateBaseUrls = () => {
  const envUrl = process.env.REACT_APP_PRODUCT_SERVICE_URL?.trim();
  const urls = envUrl ? [envUrl, ...DEFAULT_PRODUCT_URLS] : [...DEFAULT_PRODUCT_URLS];

  // Remove duplicates while preserving order
  return [...new Set(urls)];
};

const fetchWithFallback = async (path, options = {}) => {
  const candidates = getCandidateBaseUrls();
  let lastError = null;

  for (const baseUrl of candidates) {
    try {
      const response = await fetch(`${baseUrl}${path}`, options);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return { response, baseUrl };
    } catch (error) {
      lastError = error;
    }
  }

  throw lastError || new Error('Impossible de contacter le service produits');
};

export const productService = {
  // Récupérer tous les produits
  getAllProducts: async () => {
    try {
      const { response, baseUrl } = await fetchWithFallback('/api/products', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        mode: 'cors'
      });

      console.log('Fetching products from:', `${baseUrl}/api/products`);
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error fetching products:', error);
      throw error;
    }
  },

  // Récupérer un produit par ID
  getProductById: async (id) => {
    try {
      const { response } = await fetchWithFallback(`/api/products/${id}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        mode: 'cors'
      });

      return await response.json();
    } catch (error) {
      console.error('Error fetching product:', error);
      throw error;
    }
  }
};

export const getProductServiceBaseUrls = () => getCandidateBaseUrls();
