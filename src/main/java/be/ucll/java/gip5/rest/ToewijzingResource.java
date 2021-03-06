package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.*;
import be.ucll.java.gip5.dto.ToewijzingDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static be.ucll.java.gip5.util.Api.checkApiKey;

@RestController
@RequestMapping("/rest/v1")
public class ToewijzingResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private ToewijzingRepository toewijzingRepository;
    private PersoonRepository persoonRepository;
    private PloegRepository ploegRepository;
    private WedstrijdRepository wedstrijdRepository;

    @Autowired
    public ToewijzingResource(ToewijzingRepository toewijzingRepository,PersoonRepository persoonRepository,PloegRepository ploegRepository, WedstrijdRepository wedstrijdRepository){
        this.toewijzingRepository = toewijzingRepository;
        this.persoonRepository = persoonRepository;
        this.wedstrijdRepository = wedstrijdRepository;
    }

    @GetMapping(value="/toewijzing/{id}")
    public ResponseEntity getToewijzing(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        return ResponseEntity.status(HttpStatus.OK).body(checkIdAndGetToewijzing(id));
    }

    @GetMapping(value="/toewijzing")
    public ResponseEntity getToewijzingList(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        List<Toewijzing> toewijzingList = toewijzingRepository.findAll();
        if(toewijzingList.isEmpty()){
            throw new NotFoundException("Geen toewijzingen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }
    @GetMapping("/toewijzing/persoon/{persoonId}")
    public ResponseEntity getToewijzingListVanPersoon(@PathVariable("persoonId") Long persoonId,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        checkIdAndGetPersoon(persoonId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(persoonId);
        if(!toewijzingList.isPresent() || toewijzingList.get().isEmpty()){
            throw new NotFoundException("Geen toewijzingen gevonden voor persoon met id "+persoonId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList.get());
    }

    @PostMapping( value = "/toewijzing")
    public ResponseEntity postToewijzing(@RequestBody ToewijzingDTO toewijzing,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(toewijzing.equals(null)){
            throw new ParameterInvalidException("Geen toewijzing meegegeven");
        }
        checkToewijzingDto(toewijzing);
        Toewijzing newToewijzing = new Toewijzing.ToewijzingBuilder()
                .persoonId(toewijzing.getPersoonId())
                .ploegId(toewijzing.getPloegId())
                .rol(toewijzing.getRol())
                .build();
        toewijzingRepository.save(newToewijzing);
        return ResponseEntity.status(HttpStatus.OK).body(newToewijzing);
    }

    @PutMapping( value = "/toewijzing/{id}")
    public ResponseEntity putToewijzing(@PathVariable("id") Long id,@RequestBody ToewijzingDTO toewijzing,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(toewijzing.equals(null)){
            throw new ParameterInvalidException("Geen toewijzing meegegeven");
        }
        Toewijzing foundToewijzing = checkIdAndGetToewijzing(id);
        checkToewijzingDto(toewijzing);
        foundToewijzing.setPersoonId(toewijzing.getPersoonId());
        foundToewijzing.setPloegId(toewijzing.getPloegId());
        foundToewijzing.setRol(toewijzing.getRol());
        return ResponseEntity.status(HttpStatus.OK).body(foundToewijzing);
    }

    @DeleteMapping(value = "/toewijzing/{id}" )
    public ResponseEntity deleteToewijzing(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        Toewijzing toewijzing = checkIdAndGetToewijzing(id);
        toewijzingRepository.delete(toewijzing);
        return ResponseEntity.status(HttpStatus.OK).body(toewijzing);
    }

    @DeleteMapping( value = "/toewijzing/persoon/{persoonId}" )
    public ResponseEntity deleteToewijzingenVanPersoon(@PathVariable("persoonId") Long persoonId,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        checkIdAndGetPersoon(persoonId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(persoonId);
        if(!toewijzingList.isPresent()){
            throw new NotFoundException("Geen toewijzingen gevonden voor deze persoon");
        }
        toewijzingRepository.deleteAll(toewijzingList.get());
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }

    @DeleteMapping( value = "/toewijzing/ploeg/{ploegId}" )
    public ResponseEntity deleteToewijzingenVanPloeg(@PathVariable("ploegId") Long ploegId,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        checkIdAndGetPloeg(ploegId);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPloegId(ploegId);
        if(!toewijzingList.isPresent()){
            throw new NotFoundException("Geen toewijzingen gevonden voor deze ploeg");
        }
        toewijzingRepository.deleteAll(toewijzingList.get());
        return ResponseEntity.status(HttpStatus.OK).body(toewijzingList);
    }

    @DeleteMapping( value = "/toewijzing/rol/{rolId}" )
    public ResponseEntity deleteToewijzingenVanRol(@PathVariable("rolId") Rol rolId,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByRol(rolId);
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
        Optional<Persoon> persoon = persoonRepository.findPersoonById(toewijzing.getPersoonId());
        Optional<Ploeg> ploeg = ploegRepository.findPloegById(toewijzing.getPloegId());
        if(!persoon.isPresent()){
            throw new NotFoundException("Geen persoon gevonden met id "+toewijzing.getPersoonId());
        }
        if(!ploeg.isPresent()){
            throw new NotFoundException("Geen ploeg gevonden met id "+toewijzing.getPloegId());
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
}
