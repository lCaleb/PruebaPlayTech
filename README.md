# PruebaPlayTech
# User API

API REST para la gestión de usuarios construida con **Java 17**, **Spring Boot**, **PostgreSQL** y **JWT**.

---

## 🧰 Tecnologías

- Java 17
- Spring Boot 3.5.3
- PostgreSQL
- Maven
- Docker

---

## 🚀 Cómo correr el proyecto

### 🔧 Requisitos previos

- Java 17
- PostgreSQL (instalado localmente)
- Maven
- Docker

### 🧱 Configuración de base de datos

Este proyecto incluye un archivo llamado `user_api.sql` (con usuario postgres) que contiene:

- Creación de la base de datos `user_api`
- Creación de la tabla `users`
- Inserción de usuarios de prueba con contraseñas ya encriptadas (bcrypt)
- Asignación de permisos al usuario `postgres`

### 📄 Ejecutar el script

Desde tu terminal, como usuario `postgres`, ejecuta:

```bash
psql -U postgres -d postgres -f user_api.sql

▶️ Correr el backend en docker
docker build -t userbackend .
docker run -d -p 8080:8080 --name user-container userbackend

- Ruta para hacer peticiones: http://localhost:8080

## 🌐 Endpoints disponibles

La aplicación expone los siguientes endpoints según los permisos definidos:

### 🔓 Públicos (no requieren autenticación):

- **POST** `/api/auth/login` → Autenticación y generación de token JWT   
- **GET** `/api/users` → Obtener lista general de usuarios públicos  

### 🔒 Protegidos (requieren token JWT):

- **GET** `/api/user/{id}` → Obtener usuario por ID (autenticado)
- **POST** `/api/user` → Crear usuario (autenticado)
- **PUT** `/api/user/{id}` → Actualizar usuario (autenticado)
- **DELETE** `/api/user/{id}` → Eliminar usuario (autenticado)

### 📝 Uso del token

Para acceder a las rutas protegidas, incluye en los headers:


## 🔐 Autenticación (JWT)

### 1. Login de usuario

**POST** `/api/auth/login`

#### Body de ejemplo:

```json
{
  "email": "a@a.com",
  "password": "1234"
}

Uso del token (Bearer)

Para acceder a rutas protegidas, debes enviar el token JWT en el header HTTP de la siguiente forma:

Authorization: Bearer <token_obtenido_en_login>
Con este token puedes:
	•	Acceder a /api/user/{id} (GET)
	•	Crear nuevos usuarios autenticados (/api/user, POST)
	•	Actualizar y eliminar usuarios (PUT y DELETE) según los permisos configurados.

Este proyecto se puede empaquetar con Maven y desplegar como contenedor Docker.

🧑‍💻 Autor

Caleb Villadiego