# Pay_My_Buddy
L'application Pay My Buddy est une application qui permet aux utilisateurs d'envoyer de l'argent
à leurs amis ou contacts.

# Technologies utilisées
• **Java 17** - Langage de programmation  
• **Spring Boot 3.4.5** - Framework d'application  
• **Spring Data JPA** - Sauvegarde des données  
• **Spring Security** - Authentification et autorisation  
• **Maven** - Gestion des dépendances  
• **Lombok** - Simplifie le code en réduisant la répétition  
• **Thymeleaf** - Génération de pages web dynamiques  
• **MySQL** - Base de données  
• **JaCoCo** - Couverture de code Java  
• **JUnit** - Outil pour tester le code  
• **MapStruct** - Convertit facilement des objets Java entre eux  

# Fonctionnalités
• **Inscription et connexion sécurisée** des utilisateurs  
• **Ajout d'amis** pour effectuer des transactions  
• **Transfert d'argent** entre amis  
• **Gestion des comptes** et historique des transactions  
• **Protection des données** via Spring Security  

# Architecture du Projet
• **Controller** : Reçoit les demandes et envoie les réponses  
• **Service** : Gère la logique de l'application  
• **Model** : Représente les éléments du projet  
• **DTO** : Objets de transfert de données  
• **Mapper** : Transforme les objets d’un format à un autre 

# Diagrammes
• **Diagramme UML** montre les relations entre les différentes tables et leurs éléments.

![Capture](https://github.com/user-attachments/assets/6805b6a5-beb4-4bfe-88d1-8b25a781c2ab)

• **Diagramme MPD** montre comment les données de l'application sont structurées et liées.

![Diagramm_MPD](https://github.com/user-attachments/assets/87814431-9ba7-4f96-8034-80724ac688ab)

Le schéma de la base de données repose sur trois entités principales :

- **User** : Contient les informations des utilisateurs, telles que l'email, le mot de passe et le solde.
- **Transaction** : Enregistre chaque transfert d'argent, incluant le montant, la date et une description.
- **User_Connections** : Gère les liens d’amitié entre les utilisateurs.

Les relations entre ces entités sont les suivantes :

- Un utilisateur peut être connecté à plusieurs autres utilisateurs.
- Un utilisateur peut réaliser plusieurs transactions, soit en envoyant, soit en recevant de l'argent.
- Chaque transaction est associée à un expéditeur et un destinataire.

# Documentation
**Javadoc**:
<br>
La documentation Javadoc aide à mieux comprendre le code Java et à le rendre plus clair
<br>
Consulter la Javadoc qui est dans **target/site/apidocs**

# Configuration requise
- Java 17
- MySql 
- Maven 

# Installation  
- **1** Cloner le dépôt GitHub.  
- **2** Ajouter les informations MySQL dans 'application.properties'
- **3** Construire le projet : Exécuter
```bash
mvn clean package
```
- **4** Lancer l'application : Exécuter
```bash
mvn spring-boot:run
```
- **5** Accéder à l'application via l'adresse : **http://localhost:8080**  

# Tests
**Tests Unitaires** : Le projet inclut des tests unitaires pour couvrir toute les couches de l'application

Lancer les tests avec :  
```bash 
mvn test
```
Générer le rapport (Jacoco) de couverture du code avec :   
```bash
mvn jacoco:report
```
Générer le rapport (Surefire) d'affichage des tests unitaires avec : 
```bash
mvn surefire-report:report
```
Consulter les rapports qui sont dans **target/site/jacoco/** et **target/site/**

# Sécurité
L'application utilise Spring Security pour gérer l'authentification et l'autorisation des utilisateurs. 
<br>
Les mots de passe sont sécurisés grâce à un encodage avec BCrypt.

# Navigation dans l'Application
L'application utilise **Thymeleaf** pour générer plusieurs pages **HTML** dynamiques.  

- **login.html** : Page de connexion
- **register.html** : Page d'inscription
- **profil.html** : Gestion du compte utilisateur
- **userConnections** : Gestion des amis/contacts
- **transaction.html** : Transactions et suivi des paiements

# Traitement des exceptions
L'application possède un gestionnaire d'exceptions centralisé qui capte et traite certaines exceptions spécifiques comme :

- **EntityNotFoundException** : Entité introuvable
- **UserNotFoundException** : Utilisateur n'existe pas
- **FriendAlreadyExistsException** : Connexion existante entre deux utilisateurs
- **ContactAlreadyExistException** : Contact existant dans la liste de l'utilisateur
- **InsufficientBalanceException** : Solde insuffisant pour effectuer une transaction
- **SelfTransferredAmountException** : Transfert vers soi-même interdit



# Architecture des répertoires
- **src/main/java** : Répertoire du code source principal
- **src/main/resources** : Ressources de l'application(templates, CSS, application.properties et scripts SQL)
- **src/test/java** : Tests unitaires

# Accessibilité et Conformité WCAG  

L'application a été conçue pour respecter les normes **WCAG 2.1 niveau AA**, garantissant une expérience accessible et inclusive pour tous les utilisateurs.  

**Optimisations mises en place** :  

- **Contraste amélioré** : Respect des ratios WCAG pour une meilleure lisibilité.  
- **Liens identifiables** : Soulignement par défaut et surbrillance au survol dans les formulaires.  
- **Navigation au clavier** : Focus visible et optimisé pour tous les éléments interactifs.  
- **Formulaires accessibles** : Labels explicites et messages d'erreur clairs pour une meilleure compréhension.  

Grâce à ces améliorations, l'application demeure **fonctionnelle, intuitive et agréable** pour les utilisateurs.



