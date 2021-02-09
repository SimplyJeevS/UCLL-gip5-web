package be.ucll.java.gip5;

import be.ucll.java.gip5.util.SpringVars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.Instant;

@SpringBootApplication
public class Gip5StarterSpringApplication {

    @Autowired
    private SpringVars vars;

    public static void main(String[] args) {
        SpringApplication.run(Gip5StarterSpringApplication.class, args);
    }

    @PostConstruct
    public void startupApplication() {
        vars.setStartupTime(Instant.now());
    }

}
