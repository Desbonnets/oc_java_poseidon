# oc_java_poseidon

---

## ⚡ Lancer l'application

### 1. Pré-requis

- Java 17
- Maven
- MySQL (ou rien si tu veux utiliser H2)

### 2. Configuration

Modifie le fichier `application.properties` :

```properties
logging.level.org.springframework=INFO

################### DataSource Configuration ##########################
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/demo
spring.datasource.username=****
spring.datasource.password=****

################### Hibernate Configuration ##########################

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

logging.level.org.springframework.security=DEBUG

```

Pour lancer l'application utiliser `mvn spring-boot:run`.\
Pour les tests lancer `mvn test`.

### 3. Accès

| Rôle  | Username | Password |
| ----- | -------- | -------- |
| ADMIN | admin    | password    |
| USER  | user     | password     |
