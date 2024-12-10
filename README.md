# PrixBanque

PrixBanque est une application bancaire basée sur une architecture de microservices qui fournit des fonctionnalités essentielles telles que la gestion des comptes, la facturation, les relevés et les transactions.

---

## **Prérequis**

### **Outils nécessaires**
- **Java** 17 ou supérieur.
- **Maven** pour la gestion de projet.
- **MySQL** comme base de données.
- **Docker** (optionnel mais recommandé pour le déploiement).
- **Postman** pour tester les API.
- **Git** pour cloner le dépôt.

### **Configurations requises**
- Une base de données MySQL fonctionnelle.
- Ports ouverts : **8081, 8082, 8083, 8084** et **8761**.

---

## **Installation**

### **Étape 1 : Cloner le projet**
```bash
git clone https://github.com/Faymar/prixBanque.git
cd prixBanque
```

### **Étape 2 : Configurer MySQL**
Créez les bases de données nécessaires :
1. **gestion_compte_db** pour le service GestionCompte.
2. **facturation_db** pour le service Facturation.
3. **transaction_db** pour le service Transaction.

Ensuite, configurez vos identifiants MySQL dans chaque microservice :

1. Accédez au fichier `src/main/resources/application.properties` pour chaque service.
2. Mettez à jour les champs suivants :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
   spring.datasource.username=<your_mysql_username>
   spring.datasource.password=<your_mysql_password>
   ```

---

### **Étape 3 : Lancer Eureka Server**
1. Accédez au répertoire du service **Eureka** :
   ```bash
   cd server
   ```
2. Compilez le projet :
   ```bash
   mvn clean install
   ```
3. Lancez le serveur :
   ```bash
   mvn spring-boot:run
   ```
4. Vérifiez que le serveur est en cours d'exécution à cette URL : [http://localhost:8761](http://localhost:8761).

---

### **Étape 4 : Lancer les microservices**
Pour chaque service (**GestionCompte**, **Facturation**, **Transaction**), suivez ces étapes :

1. Accédez au dossier correspondant :
   ```bash
   cd <service_name>
   ```
   Exemple :
   ```bash
   cd gestionCompte
   ```

2. Compilez le projet :
   ```bash
   mvn clean install
   ```

3. Lancez le service :
   ```bash
   mvn spring-boot:run
   ```

Les services seront disponibles aux URL suivantes :
- **GestionCompte :** [http://localhost:8081](http://localhost:8081)
- **Facturation :** [http://localhost:8082](http://localhost:8082)
- **Transaction :** [http://localhost:8083](http://localhost:8083)

---

### **Étape 5 : Lancer l'API Gateway**
1. Accédez au dossier **gateway** :
   ```bash
   cd gateway
   ```
2. Compilez et lancez le Gateway :
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. Vérifiez que l'API Gateway est disponible : [http://localhost:8080](http://localhost:8080).

---

## **Endpoints**

### **GestionCompte**

#### **UserController**
1. **Créer un utilisateur :**
   - `POST /accounts/register`
   - **Payload :**
     ```json
     {
       "username": "testuser",
       "password": "securepassword"
     }
     ```

2. **Authentification :**
   - `POST /accounts/login`
   - **Payload :**
     ```json
     {
       "username": "testuser",
       "password": "securepassword"
     }
     ```

#### **AccountController**
1. **Créer un compte bancaire :**
   - `POST /accounts/create`
   - **Header :**
     ```
     Authorization: Bearer <JWT_TOKEN>
     ```
   - **Payload :**
     ```json
     {
       "accountName": "Compte courant"
     }
     ```

2. **Valider un compte :**
   - `GET /accounts/{clientId}/validate/{accountId}`

---

### **Facturation**

#### **FactureController**
1. **Créer une facture :**
   - `POST /factures/create/{clientId}/{accountId}`
   - **Header :**
     ```
     Authorization: Bearer <JWT_TOKEN>
     ```
   - **Payload :**
     ```json
     {
       "montant": 150.0,
       "description": "Abonnement mensuel"
     }
     ```

2. **Payer une facture :**
   - `POST /factures/payer/{id}`

3. **Lister toutes les factures :**
   - `GET /factures`

4. **Obtenir une facture par ID :**
   - `GET /factures/{id}`

5. **Supprimer une facture :**
   - `DELETE /factures/{id}`

---

### **Transaction**

#### **StatementController**
1. **Générer un relevé :**
   - `POST /releves/generate/{accountId}`
   - **Payload :**
     ```json
     {
       "startDate": "2024-01-01T00:00:00",
       "endDate": "2024-12-31T23:59:59"
     }
     ```

2. **Obtenir un relevé par ID :**
   - `GET /releves/{statementId}/{accountId}`

3. **Lister tous les relevés d'un compte :**
   - `GET /releves/account/{accountId}`

#### **TransactionController**
1. **Effectuer une transaction :**
   - `POST /transactions/effectuer/{accountId}`
   - **Payload :**
     ```json
     {
       "toAccountId": 2,
       "amount": 100.0,
       "description": "Paiement facture"
     }
     ```

2. **Obtenir les transactions d'un compte :**
   - `GET /transactions/{accountId}`

---

## **Démonstration**
- **Tester les APIs :** Utilisez **Postman** pour tester chaque endpoint.
- **Vérification des logs :** Les logs des services fournissent des informations en temps réel.

---

## **Contributions**
Les contributions sont les bienvenues ! Veuillez créer une branche et soumettre une **Pull Request**.
