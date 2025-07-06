# Karas Backend

A robust backend application powering **Karas POS, Inventory, and Garage Management System** — built with love, coffee, and clean code.

---

## 🚀 Getting Started

This project is designed to run in a containerized environment using **Docker** and **Docker Compose**. Follow the steps below to set up the application locally or deploy it to a live system.

---

## ⚙️ Prerequisites

Make sure you have the following installed:

- [JDK 17 LTS (Eclipse Temurin)](https://adoptium.net/temurin/releases/?version=17)
- [Docker](https://www.docker.com/)
- [Maven 3+](https://maven.apache.org/)

### Recommended IDE

We recommend using [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) — it supports all tools mentioned above out of the box.  
If you're a student, you can get the **Ultimate Edition** for free by registering with your student email.

---

## 🧪 Running the Application Locally

1. **Environment Variables**  
   Copy `.env.example` and rename it to `.env.developmental`.  
   Fill in the required values. If you need help, contact the project maintainer for a pre-configured version.

2. **Start with IntelliJ (or any modern IDE)**  
   Configure your IDE to run `docker-compose.dev.yaml` and the containers will spin up automatically.

3. **Or use the terminal**  
   From the project root directory, run:

       docker compose -f docker-compose.dev.yaml up -d

4. **Rebuilding with hot reload support**  
   The project supports **hot swapping**, so changes in the code are reflected without rebuilding the image.  
   To rebuild the image manually, use:

       docker compose -f docker-compose.dev.yaml up -d --build

---

## 📬 Postman Collection

To test the available API endpoints, contact the repository maintainer to get access to the official **Postman Collection**.

---

## 🛠 Deployment

To deploy on a live system, refer to the `docker-compose.prod.yaml` file (or similar). Make sure to update environment variables and configure services appropriately for production use.

---

## 💬 Contact

Need help or want to contribute? Reach out to the maintainer or open an issue in the repository.
