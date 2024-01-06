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
```

Windows Powershell:
```shell
echo "PATH_TO_DB=//drive/path/to/persist/postgres/data`nDB_PASSWORD=changeit`n" > .env
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

