# E-Commerce Microservices Platform

## Architecture

Cette application e-commerce est construite avec une architecture microservices en utilisant Spring Boot , Docker et React.

### Services

- **Security Service** (port 8084) : Authentification JWT et gestion des rôles
- **User Service** (port 8082) : Gestion des utilisateurs et profils
- **Product Service** (port 8081) : Catalogue des produits et gestion du stock
- **Order Service** (port 8083) : Gestion des commandes avec front-end intégré
- **Frontend** (port 3000) : Application React unifiée

### Bases de données

Chaque service a sa propre base de données MySQL :
- security_db
- user_db  
- product_db
- order_db

## Démarrage rapide

### Prérequis

- Docker et Docker Compose
- Maven (pour développement local)
- Node.js 18+ (pour développement front-end)

### Avec Docker Compose (recommandé)

```bash
# Construire et démarrer tous les services
docker-compose up --build

# Démarrer en arrière-plan
docker-compose up -d --build

# Arrêter les services
docker-compose down
```

### Développement local

```bash
# Démarrer les bases de données
docker-compose up product-db user-db order-db security-db

# Démarrer chaque service (dans des terminaux séparés)
cd product-service && mvn spring-boot:run
cd user-service && mvn spring-boot:run  
cd security-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run

# Démarrer le front-end
cd frontend && npm start
```

## Accès à l'application

- **Frontend React** : http://localhost:3000
- **API Gateway** : http://localhost:8083
- **Dashboard Admin** : http://localhost:3000/dashboard

## Comptes de démonstration

### Administrateur
- **Email** : admin@example.com
- **Mot de passe** : Admin123!

### Utilisateurs
- **Email** : jean@example.com
- **Mot de passe** : User123!

- **Email** : marie@example.com  
- **Mot de passe** : User123!

- **Email** : ahmed@example.com
- **Mot de passe** : User123!

## Fonctionnalités

### Utilisateur Simple
- Inscription et connexion
- Navigation dans le catalogue des produits
- Recherche et filtrage des produits
- Passage de commandes
- Suivi des commandes

### Administrateur
- Dashboard avec statistiques en temps réel
- Gestion des utilisateurs (CRUD, activation/désactivation)
- Gestion des produits (CRUD, gestion du stock)
- Gestion des commandes (mise à jour des statuts)
- Monitoring des services

### Communication Inter-Services
- Vérification du stock avant création de commande
- Réduction automatique du stock lors de la commande
- Validation des utilisateurs avant traitement
- Authentification centralisée avec JWT

## Technologies

### Backend
- **Spring Boot 3.x** avec Java 17
- **Spring Security** avec JWT
- **Spring Data JPA** avec Hibernate
- **Spring WebFlux** pour communication inter-services
- **MySQL** pour les bases de données
- **Maven** pour la gestion des dépendances

### Frontend
- **React 18** avec Hooks
- **React Router** pour la navigation
- **React Bootstrap** pour l'UI
- **Axios** pour les appels API
- **Toastify** pour les notifications

### Infrastructure
- **Docker** et **Docker Compose**
- **Health checks** pour monitoring
- **Volumes persistants** pour les données

## API Endpoints

### Security Service (http://localhost:8084)
- `POST /api/auth/login` - Authentification
- `GET /api/auth/validate` - Validation token
- `GET /api/auth/user/{email}` - Infos utilisateur

### User Service (http://localhost:8082)
- `POST /api/users/register` - Inscription
- `GET /api/users` - Liste utilisateurs (ADMIN)
- `GET /api/users/{id}` - Détails utilisateur
- `PUT /api/users/{id}` - Mise à jour utilisateur
- `DELETE /api/users/{id}` - Suppression utilisateur (ADMIN)

### Product Service (http://localhost:8081)
- `GET /api/products` - Catalogue produits
- `GET /api/products/{id}` - Détails produit
- `POST /api/products` - Création produit (ADMIN)
- `PUT /api/products/{id}` - Mise à jour produit (ADMIN)
- `DELETE /api/products/{id}` - Suppression produit (ADMIN)
- `PUT /api/products/{id}/reduce-stock` - Réduction stock
- `GET /api/products/search` - Recherche produits

### Order Service (http://localhost:8083)
- `GET /api/orders` - Liste commandes
- `POST /api/orders` - Création commande
- `GET /api/orders/{id}` - Détails commande
- `PUT /api/orders/{id}/status` - Mise à jour statut (ADMIN)

## Données par défaut

L'application s'initialise automatiquement avec :

### Utilisateurs
- 1 administrateur
- 4 utilisateurs (1 inactif, 1 email non vérifié)

### Produits
- 12 produits électroniques variés
- Prix en MAD
- Stocks différents
- Images Unsplash

### Commandes
- 5 commandes exemples avec statuts variés
- Différents clients et produits

## Monitoring et Logs

Les services incluent :
- **Health checks** automatiques
- **Logs structurés** avec SLF4J
- **Métriques** Spring Boot Actuator
- **Restart automatique** en cas d'échec

## Déploiement

### Production
```bash
# Build des images
docker-compose -f docker-compose.yml build

# Déploiement
docker-compose up -d

# Monitoring des logs
docker-compose logs -f [service-name]
```

### Environment variables
- `JWT_SECRET` : Clé secrète JWT
- `JWT_EXPIRATION` : Durée de validité token (ms)
- `SPRING_PROFILES_ACTIVE` : Profile Spring (dev/prod)

## Sécurité

- **Tokens JWT** avec expiration configurable
- **Rôles** ADMIN/USER avec autorisations granulaires
- **CORS** configuré pour le front-end
- **Validation** des entrées utilisateur
- **Passwords** hashés avec BCrypt

## Développement

### Structure des projets
```
gestion_ecom/
|-- product-service/
|-- user-service/
|-- security-service/
|-- order-service/
|-- frontend/
|-- docker-compose.yml
```

### Bonnes pratiques implémentées
- **Architecture microservices** avec communication synchrone
- **Séparation des responsabilités** (Controller/Service/Repository)
- **DTOs** pour transfert de données
- **Validation** des entrées avec Bean Validation
- **Gestion centralisée des exceptions**
- **Tests** unitaires et intégration
- **Documentation** API avec SpringDoc OpenAPI

## Support

Pour toute question ou problème :
1. Vérifier les logs des services concernés
2. Consulter la documentation API
3. Vérifier l'état des services avec `docker-compose ps`
4. Redémarrer les services si nécessaire
