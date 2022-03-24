FROM maven:3-openjdk-16 as build-stage
WORKDIR /opt/book-manager

# Copy all pom.xml and cache dependencies in separate layer
COPY ./pom.xml ./pom.xml
COPY backend/pom.xml backend/pom.xml
COPY frontend/pom.xml frontend/pom.xml

RUN mvn verify --fail-never

# Copy rest of the project as separate layer
COPY backend/src backend/src
COPY frontend/src frontend/src
COPY frontend/public frontend/public
COPY frontend/babel.config.js frontend/babel.config.js
COPY frontend/vue.config.js frontend/vue.config.js
COPY frontend/package.json frontend/package.json
COPY frontend/package-lock.json frontend/package-lock.json
COPY frontend/.env.development frontend/.env.development
COPY frontend/.env.production frontend/.env.production

# Create separate user for build, because embedded postgre can't run with root user
RUN chmod -R 777 /opt/book-manager
RUN useradd --system --create-home -g root builduser
USER builduser

#mvn package -Dmaven.repo.local=/root/.m2
RUN mvn package

COPY ./docker/run.sh ./docker/run.sh

FROM openjdk:16-alpine as run-stage
WORKDIR /opt
COPY --from=build-stage /opt/book-manager/backend/target/backend-0.0.1.jar ./book-manager.jar
COPY --from=build-stage /opt/book-manager/docker/run.sh ./run.sh
RUN chmod +x /opt/run.sh
ENTRYPOINT ["/opt/run.sh"]