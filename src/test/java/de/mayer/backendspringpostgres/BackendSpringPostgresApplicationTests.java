package de.mayer.backendspringpostgres;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BackendSpringPostgresApplicationTests {
    // if not defined here, another Spring instance would be started
    // and it would try to connect to the databse defined in application.properties
    @ServiceConnection
    static MyPostgresContainer sqlContainer = MyPostgresContainer.getInstance();

    @Test
    void contextLoads() {
    }

}
