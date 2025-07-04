# 🛒 E-Commerce API

A secure and scalable Spring Boot-based REST API for managing users and products, including role-based access, JWT auth, product listing, filtering by price/category, pagination, and Swagger UI for testing.

---

## 🚀 Live Demo

🔗 **Swagger UI**:https://ecommerce-api-springboot.onrender.com/swagger-ui/index.html

> Use the test credentials below to authenticate and explore APIs.

---

## 🧪 Test Credentials

Username: testuser
Password: Test@123

---

## 📦 Features

- ✅ User Registration & Login
- ✅ JWT-based Authentication
- ✅ Role-based Access Control (USER / ADMIN)
- ✅ View & Update Profile
- ✅ Product Management (Admin)
- ✅ Public Product Listing
- ✅ Filter Products by Price & Category
- ✅ Pagination Support
- ✅ Swagger UI
- ✅ Dockerized Deployment

---

## 🛠️ Tech Stack

- Spring Boot
- Spring Security + JWT
- MySQL (AWS RDS)
- Maven
- Docker
- Render Deployment

---

## 🔐 Security

- Passwords encrypted with BCrypt
- JWT-secured endpoints
- Admin-only access for product CRUD
- Public product listing with filtering

---

## 📂 API Endpoints

### 🔓 Public Endpoints (Note: Except for /login and /swagger-ui/index.html, all other APIs are restricted due to security concerns)

| Method | Endpoint             | Description               |
|--------|----------------------|---------------------------|
| POST   | `/api/auth/register` | Register a new user       |
| POST   | `/api/auth/login`    | Login and get JWT token   |
| GET    | `/api/products`      | Get all products (paginated) |
| GET    | `/api/products/{id}` | Get product by ID         |
| GET    | `/api/products/filter/category?category=Electronics` | Filter products by category |
| GET    | `/api/products/filter/price?min=100&max=1000`        | Filter products by price range |
| GET    | `/api/products/filter?category=Books&min=100&max=500`| Filter by category and price |

---

### 🔐 Authenticated Endpoints (USER / ADMIN)

| Method | Endpoint               | Description                      |
|--------|------------------------|----------------------------------|
| GET    | `/api/users/profile`   | View own profile                 |
| PUT    | `/api/users/profile`   | Update own profile               |

---

### 🔐 Admin-Only Endpoints

| Method | Endpoint                   | Description                        |
|--------|----------------------------|------------------------------------|
| GET    | `/api/users/all`           | Get all users (non-paginated)      |
| GET    | `/api/users`               | Get users (paginated)              |
| GET    | `/api/users/{id}`          | Get user by ID                     |
| DELETE | `/api/users/{id}`          | Delete user                        |
| PUT    | `/api/users/{id}/role`     | Update user role                   |
| PUT    | `/api/users/{id}/enabled`  | Enable/disable user                |
| POST   | `/api/products`            | Add new product                    |
| PUT    | `/api/products/{id}`       | Update product                     |
| DELETE | `/api/products/{id}`       | Delete product                     |

---

## 🔎 Product Filtering Examples

| Endpoint | Description |
|----------|-------------|
| `/api/products/filter/category?category=Electronics` | Filter by category |
| `/api/products/filter/price?min=100&max=1000` | Filter by price range |
| `/api/products/filter?category=Books&min=100&max=500` | Combined filter |

---

## 🐳 Docker Deployment

```bash
# Build Docker image
docker build -t ecommerce-api .

# Run container
docker run -p 8080:8080 ecommerce-api

🔐 Using Swagger with JWT
Login via /api/auth/login

Copy JWT token

Click Authorize in Swagger UI and paste:
Bearer <your_token_here>

📝 License
This project is for demo and educational use.

🙌 Contributing
Fork the repo, create a branch, and raise a pull request!

