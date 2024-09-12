# Pen And Paper DM Helper Backend with Spring Boot Web and Postgres
## Implementation of API [Pen and Paper DM Helper](https://github.com/xome/PenAndPaperHelperOpenApi)

## Getting started

### Checkout Project
```shell
git clone git@github.com:xome/PenAndPaperDmHelperBackendSpringPostgres.git
cd PenAndPaperDmHelperBackendSpringPostgres
```

### Test

Needs local docker daemon for Testcontainers
```shell
mvn test
```

### Run with Docker Compose 

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

Build images:
```shell
docker compose build
``` 

Run:
```shell
docker-compose up -d
```

Verify that app is running:
```shell
curl -X PUT localhost:8080/adventure/Testadventure
curl localhost:8080/adventures
```

### Run with Kubernetes:

Build images:
```shell
docker compose build
```

Create Secrets for DB:
```shell
kubectl create secret generic pen-and-paper-dm-helper-db-user \
  --from-literal=db_user=dmhelper \
  --from-literal=db_password=dmhelper
```

Create Pods, Services and ConfigMap:
```shell
cd .k8s
kubectl apply -f '*.yaml'
```

Then choose either single pod:
```shell
kubectl apply -f app/app-pod.yaml
```

Or ReplicaSet:
```shell
kubectl apply -f app/app-rs.yaml
```

Or Deployment:
```shell
kubectl apply -f app/app-deploy.yaml
```

Verify that app is running:
```shell
PNP_BACKEND_PORT=$(kubectl get service pen-and-paper-dm-helper-service \
  -o jsonpath='{.spec.ports[0].nodePort}')
curl -X PUT localhost:$PNP_BACKEND_PORT/adventure/Testadventure
curl localhost:$PNP_BACKEND_PORT/adventures
```


