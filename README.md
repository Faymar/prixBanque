#Guide d'installation et d'utilisation pour le projet PrixBanque
###Ce guide explique comment configurer, installer, et utiliser le projet PrixBanque sur votre environnement local.

##Prérequis
Avant de commencer, assurez-vous d'avoir les éléments suivants :

###Outils nécessaires :

Java 17 ou supérieur.
Maven.
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
