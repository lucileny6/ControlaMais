# Controla - Backend

API REST para controle financeiro pessoal, desenvolvida com Spring Boot.

---

##  Tecnologias

- Java 21+
- Spring Boot
- Spring Security (JWT)
- JPA / Hibernate
- MariaDB

---

##  Como rodar o projeto

###  Pré-requisitos

- Java 21+
- Maven
- MariaDB rodando localmente

---

###  Configuração do banco

Arquivo:

src/main/resources/application.properties

Exemplo:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/meubanco
spring.datasource.username=usuario
spring.datasource.password=senhauser
```

> Essas credenciais são apenas para desenvolvimento local.

---

###  Rodando o projeto

```bash
 ./mvnw spring-boot:run
```

ou

```bash
 mvn spring-boot:run
```

---

### Acesso

http://localhost:8080

---

## Autenticação

A API utiliza autenticação baseada em JWT.

### Rotas públicas

- /api/users/login
- /api/users/register

###  Rotas protegidas

- Todas as rotas /api/** requerem token

---

##  Testando a API

Ferramentas recomendadas:

- Postman
- Insomnia

Header necessário:

```bash
 Authorization: Bearer SEU_TOKEN
```

---

##  Integração com Frontend

Frontend disponível em:

https://github.com/lucileny6/ControlaMais-Frontend

---

##  Observações

- Projeto em desenvolvimento
- Configurações locais
- Não utilizar credenciais em produção

---

##  Autora

Lucileny Xavier

---

##  Objetivo

Projeto criado para estudo e evolução em backend e APIs REST.