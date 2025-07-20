# 🚗 Cotizador de Seguro Vehicular

Este es un sistema desarrollado en **Spring Boot WebFlux** que permite cotizar seguros vehiculares según características como marca, modelo, año, tipo de uso y edad del conductor.  
El sistema usa **JWT para seguridad**, y se apoya en **PostgreSQL** y **Redis**, todo integrado mediante **Docker**.

---

##  Tecnologías utilizadas

- Java 17
- Spring Boot 3 (WebFlux)
- PostgreSQL 15
- Redis 7
- Docker & Docker Compose
- JWT (Json Web Token)
- Swagger / OpenAPI 3
- Jacoco (Cobertura de código)
- Maven

---

##  Requisitos previos

- Tener instalado:
    - [Docker](https://www.docker.com/products/docker-desktop)
    - [Java 17](https://adoptium.net/)
    - [Maven](https://maven.apache.org/)
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/) (opcional)

---

##  ¿Cómo levantar el proyecto?

### 1. Clona el proyecto

```bash
git clone https://github.com/tuusuario/insurance-quote.git
cd insurance-quote
```

###  2. Usa Docker Compose

Esto inicia PostgreSQL, Redis y tu app en el puerto `8080`:

```bash
docker-compose up --build
```

> Para detener todo:
> ```bash
> docker-compose down
> ```

---

##  Si prefieres correrlo desde IntelliJ

Asegúrate de tener PostgreSQL y Redis ya corriendo con Docker:

```bash
docker-compose up -d redis postgres
```

Luego en IntelliJ:

- Importa el proyecto como Maven
- Ejecuta la clase `InsuranceQuoteApplication`

---

##  Configuración de base de datos

Se usará PostgreSQL en `localhost:5432`, con estas credenciales:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/vehicle_insurance
    username: postgres
    password: 1234
```

La siguiente tabla se crea automáticamente si no existe:

```sql
CREATE TABLE IF NOT EXISTS cotizaciones (
    id SERIAL PRIMARY KEY,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    anio INT,
    tipo_uso VARCHAR(50),
    edad_conductor INT,
    prima_base NUMERIC,
    prima_total NUMERIC,
    fecha_cotizacion TIMESTAMP
);
```

---

##  Seguridad con JWT

Todos los endpoints están protegidos y requieren un token JWT.

###  Login para obtener token

```http
POST /auth/login?username=testuser
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

###  Usar token en tus requests

Agrega en el header:

```
Authorization: Bearer <token>
```

---

##  Endpoint principal

### `POST /customers/cotizaciones`

**Body:**
```json
{
  "marca": "Audi",
  "modelo": "Q5",
  "anio": 2020,
  "tipoUso": "carga",
  "edadConductor": 30
}
```

**Respuesta:**
```json
{
  "data": {
    "primaBase": 500,
    "ajustes": [
      "Año > 2015 (+15%)",
      "Uso: carga (+10%)",
      "Marca Audi (+10%)"
    ],
    "primaTotal": 675.00
  },
  "message": "Cotización generada con éxito",
  "status": "SUCCESS"
}
```

---


---

##  Pruebas y cobertura

### Ejecutar pruebas unitarias

```bash
mvn test
```

### Ver cobertura de código (Jacoco)

```bash
mvn jacoco:report
```

Abrir el archivo:
```
target/site/jacoco/index.html
```

---

##  Estructura del proyecto

```
src/
  └── main/
      ├── java/
      │   └── com.vehicle.insurance_quote/
              ├── business/
                  ├── repository/
                  ├── service/
              ├── configuration/
      │       ├── controller/
      │       ├── model/
      │       ├── security/
      │       └── ...
      └── resources/
          └── OpenApi/
              ├── Cotizador.yml
          ├── application.yml
          └── schema.sql
```

---

##  `docker-compose.yml`

```yaml
version: '3.8'

services:

  postgres:
    image: postgres:15
    container_name: postgres
    environment:
      POSTGRES_DB: vehicle_insurance
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 1234
    ports:
      - "5432:5432"

  redis:
    image: redis:7
    container_name: redis
    ports:
      - "6379:6379"

  insurance-quote-app:
    build: .
    container_name: insurance-quote-app
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - redis
```

---

##  `Dockerfile`

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/insurance-quote-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

##  Configuración de seguridad JWT (`application.yml`)

```yaml
app:
  jwt:
    secret: claveSuperSecreta123
    expiration: 3600
```

---

##  Autor

**Jean Franco Constantino Chihuan Omonte**
---