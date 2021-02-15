package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.RolRepository;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Rol;
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
public class RolResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private RolRepository rolRepository;

    @Autowired
    public RolResource(RolRepository rolRepository){
        this.rolRepository = rolRepository;
    }

    @GetMapping(value = "/v1/rol/{id}")
    @Operation(
            summary = "Verkrijg rol",
            description = "Geef een rol ID en verkrijg de rol"
    )
    public ResponseEntity getRol(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor rol gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Rol> rol =  rolRepository.findRolById(id);
        if(rol.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(rol.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }
}
