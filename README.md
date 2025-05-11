#Authentication:
I set up a login endpoint at /api/auth/login for the project. By default.

#Database Setup:
For development and testing, I’m using MySQL, which is nice and lightweight. In production, though, I switch to MySQL, managed through Docker Compose.

#Performance Boost:
I’ve added indexes to the translations and translation_tags tables to keep queries snappy. The export endpoint also benefits from Caffeine caching, aiming for responses under 500ms—pretty solid!

#Testing Approach:
I’ve gone all in with JUnit tests. You’ll find unit tests for the services and integration tests for the controllers to make sure everything holds up.

#Docker Configuration:
The setup includes MySQL and Nginx containers for a production-like feel. Nginx steps in to act like a CDN, caching the export endpoint to speed things up.

#API Documentation:
Check out the Swagger UI at /swagger-ui.html for an interactive way to explore the API endpoints.

#Getting the Project:
You can package everything into a ZIP file. Just clone the repo, run mvn clean package to build the JAR, and zip up the directory when you’re ready.

#How to Download and Run:
Grab all the files and save them in the right directory structure as outlined.

Open a terminal, navigate to the project folder, and run mvn clean package to build the JAR file.

Kick things off with docker-compose up -d to start the containers in detached mode.

Once it’s up, access the API and http://localhost:8080/swagger-ui.html to dive into the Swagger docs.
