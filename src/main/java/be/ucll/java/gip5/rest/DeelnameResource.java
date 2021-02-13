package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.DeelnameRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Deelname;
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
public class DeelnameResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private DeelnameRepository deelnameRepository;

    @Autowired
    public DeelnameResource(DeelnameRepository deelnameRepository){
        this.deelnameRepository = deelnameRepository;
    }

    @GetMapping(value="/v1/deelname/{id}")
    @Operation(
            summary = "Verkrijg deelname",
            description = "Geef een deelname ID en verkrijg de deelname"
    )
    public ResponseEntity getDeelname(@PathVariable("deelname") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor deelname gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Deelname> deelname = deelnameRepository.findDeelnameById(id);
        if(!deelname.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deelname.get());
    }
}
