# Guide d'installation et d'utilisation pour le projet PrixBanque
### Ce guide explique comment configurer, installer, et utiliser le projet PrixBanque sur votre environnement local.

## Prérequis
Avant de commencer, assurez-vous d'avoir les éléments suivants :

### Outils nécessaires :

- Java 17 ou supérieur.
- Maven.
MySQL.
Docker (recommandé pour le déploiement).
Postman pour tester les API.
Git pour cloner le dépôt.
Configurations requises :

###Une base de données MySQL fonctionnelle.
Ports disponibles : 8080, 8081, 8082, et 8761 (ou les personnaliser dans le fichier application.properties).
Installation
##Étape 1 : Cloner le dépôt
bash
Copier le code
git clone https://github.com/Faymar/prixBanque.git
cd prixBanque
##Étape 2 : Configurer MySQL
Créez trois bases de données pour chaque microservice :

GestionCompte : gestion_compte_db
Facturation : facturation_db
Transaction : transaction_db
Configurez vos identifiants MySQL dans chaque microservice :

Accédez au fichier src/main/resources/application.properties de chaque service et mettez à jour :
properties
Copier le code
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
Étape 3 : Lancer Eureka Server
Eureka Server est essentiel pour la découverte des services :

Accédez au répertoire server.
Compilez le projet avec Maven :
bash
Copier le code
mvn clean install
Lancez le serveur :
bash
Copier le code
mvn spring-boot:run
Eureka sera disponible sur http://localhost:8761.

Étape 4 : Lancer les microservices
Pour chaque microservice (GestionCompte, Facturation, Transaction), suivez ces étapes :

Accédez au dossier correspondant :

bash
Copier le code
cd <service_name>
Exemple :

bash
Copier le code
cd gestionCompte
Compilez le projet :

bash
Copier le code
mvn clean install
Lancez le service :

bash
Copier le code
mvn spring-boot:run
Les microservices seront disponibles sur les ports suivants :

GestionCompte : http://localhost:8081
Facturation : http://localhost:8082
Transaction : http://localhost:8083
Étape 5 : Lancer le Gateway API
Le Gateway API centralise les requêtes vers tous les microservices :

Accédez au dossier gateway.
Compilez et lancez le Gateway API :
bash
Copier le code
mvn clean install
mvn spring-boot:run
Le Gateway sera disponible sur http://localhost:8080.


GestionCompte
UserController
Créer un utilisateur (enregistrer)

Méthode : POST
URL : /accounts/register
Description : Permet aux clients de créer un compte utilisateur.
Payload :
json
Copier le code
{
  "username": "testuser",
  "password": "securepassword"
}
Authentification (login)

Méthode : POST
URL : /accounts/login
Description : Permet aux utilisateurs de s'authentifier et d'obtenir un token JWT.
Payload :
json
Copier le code
{
  "username": "testuser",
  "password": "securepassword"
}
AccountController
Créer un compte bancaire

Méthode : POST
URL : /accounts/create
Description : Permet à un utilisateur connecté de créer un compte bancaire.
Header :
makefile
Copier le code
Authorization: Bearer <JWT_TOKEN>
Payload :
json
Copier le code
{
  "accountName": "Compte courant"
}
Valider l'appartenance d'un compte à un utilisateur

Méthode : GET
URL : /accounts/{clientId}/validate/{accountId}
Description : Vérifie si un compte bancaire appartient à un utilisateur donné.
Exemple :
bash
Copier le code
GET /accounts/1/validate/101
Facturation
FactureController
Créer une facture

Méthode : POST
URL : /factures/create/{clientId}/{accountId}
Description : Permet de créer une facture pour un utilisateur et un compte spécifiques.
Header :
makefile
Copier le code
Authorization: Bearer <JWT_TOKEN>
Payload :
json
Copier le code
{
  "montant": 150.0,
  "description": "Abonnement mensuel"
}
Payer une facture

Méthode : POST
URL : /factures/payer/{id}
Description : Permet de marquer une facture comme payée.
Exemple :
bash
Copier le code
POST /factures/payer/1
Lister toutes les factures

Méthode : GET
URL : /factures
Description : Permet de récupérer toutes les factures.
Obtenir une facture par son ID

Méthode : GET
URL : /factures/{id}
Description : Récupère les détails d'une facture spécifique.
Exemple :
bash
Copier le code
GET /factures/1
Supprimer une facture

Méthode : DELETE
URL : /factures/{id}
Description : Supprime une facture par son ID.
Exemple :
bash
Copier le code
DELETE /factures/1
Transaction
StatementController
Générer un relevé

Méthode : POST
URL : /releves/generate/{accountId}
Description : Génère un relevé bancaire pour un compte spécifique entre deux dates.
Payload :
json
Copier le code
{
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59"
}
Obtenir un relevé par ID

Méthode : GET
URL : /releves/{statementId}/{accountId}
Description : Récupère un relevé bancaire par son ID et l'ID du compte.
Lister tous les relevés d'un compte

Méthode : GET
URL : /releves/account/{accountId}
Description : Permet de récupérer tous les relevés d'un compte.
TransactionController
Effectuer une transaction

Méthode : POST
URL : /transactions/effectuer/{accountId}
Description : Effectue une transaction à partir d'un compte donné.
Payload :
json
Copier le code
{
  "toAccountId": 2,
  "amount": 100.0,
  "description": "Paiement facture"
}
Obtenir les transactions d'un compte

Méthode : GET
URL : /transactions/{accountId}
Description : Récupère toutes les transactions effectuées à partir d'un compte.
