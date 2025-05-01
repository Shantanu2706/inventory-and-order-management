# 🧾 Inventory and Order Management System – Backend (Spring Boot)

A secure and scalable backend application for managing products, orders, stock levels, and admin reports. This system is built using Spring Boot with JWT-based authentication and role-based authorization for Admin and User access.

---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **Postman** (for API testing)

---

## 🔐 Authentication & Authorization

- **Register/Login** using JWT tokens
- **Role-Based Access Control**:
  - `ADMIN`: Can manage products, view all orders, generate reports
  - `USER`: Can view products and place orders

---

## 📦 Key Features

- ✅ JWT-based login and registration
- ✅ Role-based access (Admin/User)
- ✅ Product CRUD (Create, Read, Update, Delete)
- ✅ Real-time stock updates when orders are placed
- ✅ Order placement with multiple items and automatic stock reduction
- ✅ Order history for users and all orders view for admins
- ✅ Admin reports: sales data, order summaries, stock status
- ✅ Global error handling for consistent API responses

---

## 🛠️ How to Run Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/inventory-order-backend.git
   
2. Set up the database
   Create a MySQL database (e.g., inventory_db)
   Update the application.properties file with your DB credentials
   
4. Build and run the project
   mvn clean install
   mvn spring-boot:run

5. Test with Postman
   Use token-based authentication headers
   Test all endpoints
