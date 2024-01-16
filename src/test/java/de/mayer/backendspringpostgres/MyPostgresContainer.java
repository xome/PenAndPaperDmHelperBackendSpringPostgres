package de.mayer.backendspringpostgres;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MyPostgresContainer extends PostgreSQLContainer<MyPostgresContainer> {

    private static MyPostgresContainer container;

    public MyPostgresContainer(){
        super(DockerImageName.parse("postgres:latest"));
    }

    public static MyPostgresContainer getInstance(){
        if (container == null){
            container = new MyPostgresContainer()
                    .withInitScript("ddl.sql")
                    .withReuse(true);
        }
        container.start();
        return container;
    }

}
