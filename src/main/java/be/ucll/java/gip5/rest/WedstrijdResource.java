package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Wedstrijd;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WedstrijdResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    public WedstrijdResource(WedstrijdRepository wedstrijdRepository){
        this.wedstrijdRepository = wedstrijdRepository;
    }

    @GetMapping(value = "/v1/wedstrijd/{id}")
    @Operation(
            summary = "Verkrijg wedstrijd",
            description = "Geef een wedstrijd ID en verkrijg de wedstrijd"
    )
    public ResponseEntity getRol(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor wedstrijd gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Wedstrijd> wedstrijd =  wedstrijdRepository.findWedstrijdById(id);
        if(wedstrijd.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(wedstrijd.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }
}
