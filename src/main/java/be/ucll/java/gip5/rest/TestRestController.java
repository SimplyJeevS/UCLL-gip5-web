package be.ucll.java.gip5.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class TestRestController {
    private Logger logger = LoggerFactory.getLogger(TestRestController.class);

    // URL => http://<server and port>/rest/v1/test
    @GetMapping(value = "/v1/test")
    public ResponseEntity getTestResponse() {
        logger.debug("Rest service was triggered");
        return new ResponseEntity<String>("Hello from UCLL Gip5 Starter Rest service", HttpStatus.OK);
    }
}

