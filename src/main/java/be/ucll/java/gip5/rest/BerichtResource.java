package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.BerichtRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Bericht;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BerichtResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private BerichtRepository berichtRepository;

    @Autowired
    public BerichtResource(BerichtRepository berichtRepository){
        this.berichtRepository = berichtRepository;
    }

    @GetMapping(value = "/v1/bericht/{id}")
    @Operation(
            summary = "Verkrijg bericht",
            description = "Geef een bericht ID en verkrijg het bericht"
    )
    public ResponseEntity getBericht(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor bericht gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Bericht> bericht =  berichtRepository.findBerichtById(id);
        if(bericht.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(bericht.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

}
