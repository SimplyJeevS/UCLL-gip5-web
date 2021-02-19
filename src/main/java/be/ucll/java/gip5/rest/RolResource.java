package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.RolRepository;
import be.ucll.java.gip5.dto.RolDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Rol;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/v1")
public class RolResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private RolRepository rolRepository;

    @Autowired
    public RolResource(RolRepository rolRepository){
        this.rolRepository = rolRepository;
    }

    @GetMapping(value = "/rol/{id}")
    @Operation(
            summary = "Verkrijg rol",
            description = "Geef een rol ID en verkrijg de rol"
    )
    public ResponseEntity getRol(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor rol gekregen");
        return ResponseEntity.status(HttpStatus.OK).body(checkAndFindRolFromId(id));
    }
    @PostMapping( value = "/rol")
    public ResponseEntity postRol(@RequestBody RolDTO rol) throws ParameterInvalidException {
        checkRolNaam(rol.getNaam());
        List<Rol> rolList = rolRepository.findAll();
        Rol newRol = new Rol.RolBuilder().naam(rol.getNaam()).build();
        rolRepository.save(newRol);
        return ResponseEntity.status(HttpStatus.OK).body(newRol);
    }

    @PutMapping( value = "/rol/{id}")
    public ResponseEntity putRol(@PathVariable("id") Long id, @RequestBody RolDTO rolDTO) throws NotFoundException, ParameterInvalidException {
        Rol rol = checkAndFindRolFromId(id);
        checkRolNaam(rol.getNaam());
        rol.setNaam(rol.getNaam());
        rolRepository.save(rol);
        return ResponseEntity.status(HttpStatus.OK).body(rol);
    }

    @GetMapping( value = "/rol")
    public ResponseEntity getRolList() throws NotFoundException {
        List<Rol> rolList = rolRepository.findAll();
        if(rolList.isEmpty()){
            throw new NotFoundException("Er zijn geen rollen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(rolList);
    }

    @DeleteMapping( value="/rol/{id}")
    public ResponseEntity deleteRol(@PathVariable("id") Long id) throws NotFoundException, ParameterInvalidException {
        Rol rol = checkAndFindRolFromId(id);
        rolRepository.delete(rol);
        return ResponseEntity.status(HttpStatus.OK).body(rol);
    }

    private Rol checkAndFindRolFromId(Long id) throws ParameterInvalidException, NotFoundException {

        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Rol> rol =  rolRepository.findRolById(id);
        if(rol.isPresent()){
            return rol.get();
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    private void checkRolNaam(String naam) throws ParameterInvalidException {
        if(naam.isEmpty() || naam.trim().length() <= 2){
            throw new ParameterInvalidException("Rol naam moet minstens 3 karakters hebben, kreeg "+naam);
        }
    }

}
