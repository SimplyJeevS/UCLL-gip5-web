package be.ucll.java.gip5.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/ping")
public class PingResource {
    private Logger logger = LoggerFactory.getLogger(PingResource.class);

    @GetMapping(value = "")
    @Operation(
            summary = "ping",
            description = "Should receive 'pong'"
    )
    public ResponseEntity getPingResource() {
        logger.debug("Rest ping service was triggered");
        return ResponseEntity.status(HttpStatus.OK).body("pong");
    }
}

