# Karas Backend
A backend application for Karas POS, Inventory and Garage Management System. Built with love and coffeeee.

## Getting Started
You will most likely need to deploy and run this using docker and docker-compose on your local machine. See deployment
section for deploying this on a live system
### Prerequisites
Requirements to deploy and run this project
- [JDK 17 LTS (Eclipse Temurin)](https://adoptium.net/temurin/releases/?version=17)
- [Docker](https://www.docker.com/)
- [Maven 3](https://maven.apache.org/)

Consider using an IDE like [IntelliJ](https://www.jetbrains.com/idea/download/) to configure everything above. 
Really, it has everything outside the box. It is recommended to get the ultimate version through a student license
by registering with your student email
### Running the application locally
Use `.env.example` to create and fill a file named `.env.developmental` in the project root directory.
You can ask the maintainer of this project obtain a pre-existing env config to it up and running quickly.

If you are using an IDE like IntelliJ or any other equivalent IDE. Set it to run
```docker-compose.dev.yaml``` and everything will be handled for you.

Otherwise, you can use your terminal to deploy. From the project root directory, simply run
```BASH
docker compose -f docker-compose.dev.yaml up -d
```

There is hot-swapping (or hot reloading), so you don't need to redeploy the container image every time you made some 
changes.

To rebuild the container image and deploy it, run
```BASH
docker compose -f docker-compose.dev.yml up -d --build
```

## Postman Collection
Contact the owner of this repository to gain access to the Postman's collection of routes for testing purposes.

## Deployment
TBA

## License
TBA