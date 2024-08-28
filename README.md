# OnlineBookstoreManagementSystem

# **Book Management System**

## **Project Overview**

The Book Management System is a comprehensive web application designed to manage and facilitate the sale and loaning of books. Built with a React frontend and a Java backend, this system provides a seamless experience for both customers and administrators. Customers can browse through an extensive collection of books, purchase or loan them using various payment methods, including direct payment or through a subscription model. Administrators can efficiently manage the book inventory, track transactions, and manage customer subscriptions.

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
- **Purchase and Loan Options**: Customers can either buy or loan books. Loaning can be done through a direct payment or via a subscription model.
- **Subscription Management**: Users can subscribe to various plans which allow them to loan books on a regular basis.

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
   git clone https://github.com/your-repo/book-management-system.git
   cd book-management-system

