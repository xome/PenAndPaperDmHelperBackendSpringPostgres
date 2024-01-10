# Pen And Paper DM Helper Backend with Spring Boot Web and Postgres
## Implementation of API [Pen and Paper DM Helper](https://github.com/xome/PenAndPaperHelperOpenApi)

## Getting started
### Get Source and use Docker Compose 

Checkout Project:
```shell
git clone git@github.com:xome/PenAndPaperDmHelperBackendSpringPostgres.git
cd PenAndPaperDmHelperBackendSpringPostgres
```

Define local directory for database files and password for connection:

Unix:
```shell
printf "PATH_TO_DB=/path/to/persist/postgres/data\n" > .env
printf "DB_PASSWORD=changeit\n" >> .env
printf  "DB_USER=dmhelper\n" >> .env
printf  "DB_URL=db\n" >> .env
printf  "DB_NAME=dmhelper\n" >> .env
printf  "DB_PORT=5432\n" >> .env
printf  "APP_PORT=8080\n" >> .env
```

Windows Powershell:
```shell
echo "PATH_TO_DB=//drive/path/to/persist/postgres/data`nDB_PASSWORD=changeit`nDB_USER=dmhelper`nDB_URL=db`nDB_NAME=dmhelper`nDB_PORT=5432`nAPP_PORT=8080`n`" > .env
```

Build local images if you like (optional, you will pull from docker hub if you don't build)
```shell
mvn -B --file pom.xml spring-boot:build-image -Dspring-boot.build-image.imageName=xome42/pen-and-paper-dm-helper-backend-spring-postgres
docker build DockerfileDatabase -t xome42/pen-and-paper-dm-helper-db
```

Run Docker Compose:
```shell
docker-compose up
```

Try to create an Adventure with curl:
```shell
curl -X PUT localhost:8080/adventure/Testadventure
```

See if the Adventure was saved:
```shell
curl localhost:8080/adventures
```

