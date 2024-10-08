# OnlineBookstoreManagementSystem

# **Book Management System**

## **Project Overview**

The Book Management System is a comprehensive web application designed to manage and facilitate the sale of books. Built with a React frontend and a Java backend, this system provides a seamless experience for both customers and administrators. Customers can browse through an extensive collection of books, purchase them using a payment method. l. Administrators can efficiently manage the book inventory, track transactions, and manage users.

### Architectural Design
![image](https://github.com/user-attachments/assets/12cd736c-0284-4dbd-a194-1d89cdd4fa6d)
![image (1)](https://github.com/user-attachments/assets/6bda09ab-d7cb-4713-b386-ee98fde9cfee)
![image](https://github.com/user-attachments/assets/7be685eb-e60d-4876-928c-da72d8143e47)


## **Creators**

This project was collaboratively developed by:
- **Ismet Ymeri**
- **Samuli Lamminmäki**
- **Stefanos Thomas**
- **Onni Luova**

## **Features**

### **Customer Features**
- **Book Browsing**: Users can browse a wide range of books categorized by genre, author, and popularity.
- **Search Functionality**: A robust search engine allows users to quickly find specific books.
- **Book Details**: Each book has a detailed page including the author, description, price, and availability.
- **Purchase**: Customers can buy books.
- **Subscription Management**: Users can signup,browse and buy books.

### **Admin Features**
- **Book Inventory Management**: Admins can add, update, and delete books from the inventory.
- **Transaction Tracking**: Detailed logs of all purchases and loans are maintained.
- **User Management**: Admins can manage user accounts and their subscription statuses.
- **Analytics Dashboard**: Provides insights into book sales, loans, and overall customer engagement.

## **Technologies Used**

### **Frontend**
- **React**: A JavaScript library for building user interfaces. The entire frontend is built using React, providing a dynamic and responsive experience.
- **React Router**: Used for navigating between different pages and maintaining the history of browsing.
- **Axios**: For making API requests from the frontend to the backend.

### **Backend**
- **Java**: The core backend logic is implemented in Java, ensuring a robust and scalable application.
- **Spring Boot**: Simplifies the development of the backend by providing pre-configured setups for the project.
- **JDBC**: Used for connecting to the MariaDB database and performing CRUD operations.
- **MariaDB**: The relational database used to store all the data related to books, users, and transactions.

### **Tools and Libraries**
- **Maven**: For managing the project's dependencies and building the application.
- **Hibernate**: An ORM tool for managing database operations in a more object-oriented manner.
- **Thymeleaf**: Used in some parts of the admin interface for server-side rendering.
- **Git**: Version control for collaborative development.

## **Setup Instructions**

### **Prerequisites**
- **Node.js**: Required for running the React frontend.
- **Java JDK 17+**: Required for running the Java backend.
- **MariaDB**: The database where all application data is stored.
- **Maven**: For managing dependencies and building the Java project.

### **Installation**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/kismet85/OnlineBookstoreManagementSystem.git
   cd file/repo

2. **Setup Backend**:

***Navigate to the backend directory***:

**mvn clean install**
**Run the backend server**:

***mvn spring-boot:run***

3. **Setup Frontend**:

***Navigate to the frontend directory***:

**npm install**
**Run the React development server:**

**npm run dev**

4. **Access the Application**:

### ***Open your browser and go to http://localhost:5173 for the frontend.***
## ***The backend should be running at http://localhost:8080.***
# Usage
**Customer Interface: Browse books, add them to your cart, and proceed to purchase them.
Admin Interface: Manage the book inventory, track sales and manage users.**


## Contact
**For any questions, feedback, or suggestions, please contact the project creators:**

# Ismet Ymeri
# Samuli Lamminmäki
# Stefanos Thomas 
# Onni Luova 
