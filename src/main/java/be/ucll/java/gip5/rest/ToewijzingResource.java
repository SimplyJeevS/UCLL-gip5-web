package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.*;
import be.ucll.java.gip5.dto.ToewijzingDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.*;
import io.swagger.v3.oas.annotations.Operation;
import liquibase.pro.packaged.T;
import org.atmosphere.config.service.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/toewijzing")
public class ToewijzingResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private ToewijzingRepository toewijzingRepository;
    private PersoonRepository persoonRepository;
    private RolRepository rolRepository;
    private PloegRepository ploegRepository;
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    public ToewijzingResource(ToewijzingRepository toewijzingRepository,PersoonRepository persoonRepository,RolRepository rolRepository,PloegRepository ploegRepository, WedstrijdRepository wedstrijdRepository){
        this.toewijzingRepository = toewijzingRepository;
        this.persoonRepository = persoonRepository;
        this.rolRepository = rolRepository;
        this.wedstrijdRepository = wedstrijdRepository;
    }

    @GetMapping(value="/{id}")
    @Operation(
            summary = "Verkrijg een toewijzing",
            description = "Geef een toewijzing Id en verkrijg een toewijzing"
    )
    public ResponseEntity getToewijzing(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(checkIdAndGetToewijzing(id));
    }

    @GetMapping(value="/")
    public ResponseEntity getToewijzingList() throws NotFoundException {
        List<Toewijzing> toewijzingList = toewijzingRepository.findAll();
        if(toewijzingList.isEmpty()){
            throw new NotFoundException("Geen toewijzingen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }
    @GetMapping("/persoon/{persoonId}")
    public ResponseEntity getToewijzingListVanPersoon(@PathVariable("persoonId") Long persoonId) throws NotFoundException, ParameterInvalidException {
        checkIdAndGetPersoon(persoonId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(persoonId);
        if(!toewijzingList.isPresent() || toewijzingList.get().isEmpty()){
            throw new NotFoundException("Geen toewijzingen gevonden voor persoon met id "+persoonId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList.get());
    }

    @PostMapping( value = "/")
    public ResponseEntity postToewijzing(@RequestBody ToewijzingDTO toewijzing) throws ParameterInvalidException, NotFoundException {
        if(toewijzing.equals(null)){
            throw new ParameterInvalidException("Geen toewijzing meegegeven");
        }
        checkToewijzingDto(toewijzing);
        Toewijzing newToewijzing = new Toewijzing.ToewijzingBuilder()
                .persoonId(toewijzing.getPersoonId())
                .ploegId(toewijzing.getPloegId())
                .rolId(toewijzing.getRolId())
                .build();
        toewijzingRepository.save(newToewijzing);
        return ResponseEntity.status(HttpStatus.OK).body(newToewijzing);
    }

    @PutMapping( value = "/{id}")
    public ResponseEntity putToewijzing(@PathVariable("id") Long id,@RequestBody ToewijzingDTO toewijzing) throws ParameterInvalidException, NotFoundException {
        if(toewijzing.equals(null)){
            throw new ParameterInvalidException("Geen toewijzing meegegeven");
        }
        Toewijzing foundToewijzing = checkIdAndGetToewijzing(id);
        checkToewijzingDto(toewijzing);
        foundToewijzing.setPersoonId(toewijzing.getPersoonId());
        foundToewijzing.setPloegId(toewijzing.getPloegId());
        foundToewijzing.setRolId(toewijzing.getRolId());
        return ResponseEntity.status(HttpStatus.OK).body(foundToewijzing);
    }

    @DeleteMapping(value = "/{id}" )
    public ResponseEntity deleteToewijzing(@PathVariable("id") Long id) throws NotFoundException, ParameterInvalidException {
        Toewijzing toewijzing = checkIdAndGetToewijzing(id);
        toewijzingRepository.delete(toewijzing);
        return ResponseEntity.status(HttpStatus.OK).body(toewijzing);
    }

    @DeleteMapping( value = "/persoon/{persoonId}" )
    public ResponseEntity deleteToewijzingenVanPersoon(@PathVariable("persoonId") Long persoonId) throws NotFoundException, ParameterInvalidException {
        checkIdAndGetPersoon(persoonId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(persoonId);
        if(!toewijzingList.isPresent()){
            throw new NotFoundException("Geen toewijzingen gevonden voor deze persoon");
        }
        toewijzingRepository.deleteAll(toewijzingList.get());
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }

    @DeleteMapping( value = "/ploeg/{ploegId}" )
    public ResponseEntity deleteToewijzingenVanPloeg(@PathVariable("ploegId") Long ploegId) throws NotFoundException, ParameterInvalidException {
        checkIdAndGetPloeg(ploegId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPloegId(ploegId);
        if(!toewijzingList.isPresent()){
            throw new NotFoundException("Geen toewijzingen gevonden voor deze ploeg");
        }
        toewijzingRepository.deleteAll(toewijzingList.get());
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }

    @DeleteMapping( value = "/rol/{rolId}" )
    public ResponseEntity deleteToewijzingenVanRol(@PathVariable("rolId") Long rolId) throws NotFoundException, ParameterInvalidException {
        checkIdAndGetRol(rolId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByRolId(rolId);
        if(!toewijzingList.isPresent()){
            throw new NotFoundException("Geen toewijzingen gevonden voor deze rol");
        }
        toewijzingRepository.deleteAll(toewijzingList.get());
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }

    private void checkToewijzingDto(ToewijzingDTO toewijzing) throws ParameterInvalidException, NotFoundException {
        if(toewijzing.getPersoonId().equals(null) || toewijzing.getPersoonId() <= 0 ){
            throw new ParameterInvalidException("PersoonId moet een positief getal zijn");
        }
        if(toewijzing.getPloegId().equals(null) || toewijzing.getPloegId() <= 0 ){
            throw new ParameterInvalidException("RolId moet een positief getal zijn");
        }
        if(toewijzing.getRolId().equals(null) || toewijzing.getRolId() <= 0 ){
            throw new ParameterInvalidException("PloegId moet een positief getal zijn");
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(toewijzing.getPersoonId());
        Optional<Ploeg> ploeg = ploegRepository.findPloegById(toewijzing.getPloegId());
        Optional<Rol> rol = rolRepository.findRolById(toewijzing.getRolId());
        if(!persoon.isPresent()){
            throw new NotFoundException("Geen persoon gevonden met id "+toewijzing.getPersoonId());
        }
        if(!ploeg.isPresent()){
            throw new NotFoundException("Geen ploeg gevonden met id "+toewijzing.getPloegId());
        }
        if(!rol.isPresent()){
            throw new NotFoundException("Geen rol gevonden met id "+toewijzing.getRolId());
        }
    }

    private Toewijzing checkIdAndGetToewijzing(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Toewijzing> toewijzing = toewijzingRepository.findToewijzingById(id);
        if(!toewijzing.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return toewijzing.get();
    }
    private Persoon checkIdAndGetPersoon(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(id);
        if(!persoon.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return persoon.get();
    }

    private Ploeg checkIdAndGetPloeg(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> ploeg = ploegRepository.findPloegById(id);
        if(!ploeg.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return ploeg.get();
    }

    private Rol checkIdAndGetRol(Long id) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Rol> rol = rolRepository.findRolById(id);
        if(!rol.isPresent()){
            throw new NotFoundException(id.toString());
        }
        return rol.get();
    }
}
