package be.ucll.java.gip5;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
public abstract class AbstractIntegrationTest {

    private static PostgreSQLContainer POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.username", () -> "postgres");
        dynamicPropertyRegistry.add("spring.datasource.password", () -> "postgres");
        dynamicPropertyRegistry.add("spring.datasource.url",
                () -> String.format(
                        "jdbc:postgresql://localhost:%d/postgres",
                        POSTGRES.getMappedPort(5432)
                )
        );
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String input, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(input, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromMvcResult(MvcResult result, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(result.getResponse().getContentAsString(), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}