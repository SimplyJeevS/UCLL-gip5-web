package be.ucll.java.gip5.util;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SpringVars {

    private Instant startupTime;

    public Instant getStartupTime() {
        return startupTime;
    }

    public void setStartupTime(Instant startupTime) {
        this.startupTime = startupTime;
    }
}
