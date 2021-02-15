package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Deelname;
import be.ucll.java.gip5.model.Toewijzing;
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
@RequestMapping("/v1/toewijzing")
public class ToewijzingResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private ToewijzingRepository toewijzingRepository;

    @Autowired
    public ToewijzingResource(ToewijzingRepository toewijzingRepository){
        this.toewijzingRepository = toewijzingRepository;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Verkrijg een toewijzing",
            description = "Geef een toewijzing Id en verkrijg een toewijzing"
    )
    public ResponseEntity getToewijzing(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor toewijzing gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Toewijzing> toewijzing = toewijzingRepository.findToewijzingById(id);
        if(!toewijzing.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(toewijzing.get());

    }
}
