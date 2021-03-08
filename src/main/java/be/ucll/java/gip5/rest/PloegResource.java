package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Toewijzing;
import be.ucll.java.gip5.model.Wedstrijd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.ucll.java.gip5.util.Api.checkApiKey;

@RestController
@RequestMapping("/rest/v1")
public class PloegResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PloegRepository ploegRepository;
    private WedstrijdRepository wedstrijdRepository;
    private PersoonRepository persoonRepository;
    private ToewijzingRepository toewijzingRepository;
    private Locale loc = new Locale("en");

    @Autowired
    public PloegResource(PloegRepository ploegRepository, WedstrijdRepository wedstrijdRepository, PersoonRepository persoonRepository, ToewijzingRepository toewijzingRepository){
        this.ploegRepository = ploegRepository;
        this.wedstrijdRepository = wedstrijdRepository;
        this.persoonRepository = persoonRepository;
        this.toewijzingRepository = toewijzingRepository;
    }

    @GetMapping(value = "/ploeg/{id}")
    public ResponseEntity getPloeg(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        logger.debug("GET request voor ploeg gekregen");
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> ploeg =  ploegRepository.findPloegById(id);
        if(ploeg.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(ploeg.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    @GetMapping( value = "/ploeg")
    public ResponseEntity getPloegen(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        List<Ploeg> ploegen = ploegRepository.findAll();
        if(ploegen.isEmpty()){
            throw new NotFoundException("Geen ploegen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }

    @GetMapping(value="/ploeg/{id}/spelers")
    public ResponseEntity getAllSpelersInPloeg(@PathVariable("id") Long id, @RequestParam(name = "api", required = false, defaultValue = "") String api) throws InvalidCredentialsException, NotFoundException {
        checkApiKey(api,persoonRepository);
        Optional<Ploeg> ploeg = ploegRepository.findPloegById(id);
        if(!ploeg.isPresent()){
            throw new NotFoundException("Ploeg met de meegegeven id bestaat niet");
        }
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPloegId(id);
        if(!toewijzingList.isPresent()){
            throw new NotFoundException("Er zijn geen toewijzingen gevonden voor de meegegeven ploeg id");
        }
        List<Persoon> persoonList = new ArrayList<>();
        for (Toewijzing t : toewijzingList.get()) {
            persoonList.add(persoonRepository.findPersoonById(t.getPersoonId()).get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(persoonList);
    }

    @PostMapping(value="/ploeg")
    public ResponseEntity postPloeg(@RequestBody PloegDTO ploeg,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        checkIfPloegExists(ploeg.getNaam());
        checkPloegDTO(ploeg);
        Ploeg newPloeg = ploegRepository.save(
                new Ploeg.PloegBuilder()
                .naam(ploeg.getNaam())
                .omschrijving(ploeg.getOmschrijving())
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newPloeg);
    }

    @PutMapping( value = "/ploeg/{id}")
    public ResponseEntity putPloeg(@PathVariable("id") Long id,@RequestBody PloegDTO ploeg,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> foundPloeg = ploegRepository.findPloegById(id);
        if(!foundPloeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+id);
        }
        checkPloegDTO(ploeg);
        foundPloeg.get().setNaam(ploeg.getNaam());
        foundPloeg.get().setOmschrijving(ploeg.getOmschrijving());
        ploegRepository.save(foundPloeg.get());
        return ResponseEntity.status(HttpStatus.OK).body(foundPloeg);
    }

    @DeleteMapping( value = "/ploeg/{id}")
    public ResponseEntity deletePloeg(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> foundPloeg = ploegRepository.findPloegById(id);
        if(!foundPloeg.isPresent()){
            throw new NotFoundException("Ploeg met id "+id);
        }
        ploegRepository.delete(foundPloeg.get());
        return ResponseEntity.status(HttpStatus.OK).body(foundPloeg);
    }

    private void checkPloegDTO(PloegDTO ploeg) throws ParameterInvalidException {
        if(ploeg.getNaam().isEmpty() || ploeg.getNaam().trim().length() ==0){
            throw new ParameterInvalidException("Naam met value "+ploeg.getNaam());
        }
    }

    @GetMapping( value = "/ploeg/thuisploeg")
    public ResponseEntity getThuisploegen(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        List<Wedstrijd> wedstrijden = wedstrijdRepository.findAll();
        if(wedstrijden.isEmpty()){
            throw new NotFoundException("Er zijn nog geen wedstrijden gespeeld");
        }
        List<Ploeg> ploegen = Collections.<Ploeg>emptyList();
        wedstrijden.forEach(wedstrijd -> {
            Optional<Ploeg> ploeg = ploegRepository.findPloegById(wedstrijd.getThuisPloeg());
            if(ploeg.isPresent()){
                ploegen.add(ploeg.get());
            }
        });
        if(ploegen.isEmpty()){
            throw new NotFoundException("Thuisploegen in wedstrijden ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }

    @GetMapping( value = "/ploeg/tegenstander")
    public ResponseEntity getTegenstanders(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        List<Wedstrijd> wedstrijden = wedstrijdRepository.findAll();
        if(wedstrijden.isEmpty()){
            throw new NotFoundException("Er zijn nog geen wedstrijden gespeeld");
        }
        List<Ploeg> ploegen = Collections.<Ploeg>emptyList();
        wedstrijden.forEach(wedstrijd -> {
            Optional<Ploeg> ploeg = ploegRepository.findPloegById(wedstrijd.getTegenstander());
            if(ploeg.isPresent()){
                ploegen.add(ploeg.get());
            }
        });
        if(ploegen.isEmpty()){
            throw new NotFoundException("Tegenstander in wedstrijden ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ploegen);
    }
    public void setLocale(Locale loc) {this.loc = loc;
    }
    private List<PloegDTO> queryListToPloegDtoList(List<Ploeg> lst){
        Stream<PloegDTO> stream = lst.stream()
                .map(rec -> {
                    PloegDTO dto = new PloegDTO();
                    dto.setId(rec.getId());
                    dto.setNaam(rec.getNaam());
                    dto.setOmschrijving(rec.getOmschrijving());
                    return dto;
                });
        return stream.collect(Collectors.toList());
    }
    public List<PloegDTO> getAllPloegen() {return queryListToPloegDtoList(ploegRepository.findAll());}
    public List<PloegDTO> getSearchPloegen(String naam) throws IllegalArgumentException {
        if (naam == null || naam.trim().length() == 0)
            throw new IllegalArgumentException("Personen ophalen met de naam gefaald. Naam leeg");
        List<Ploeg> lst = ploegRepository.getAllByNaamContainingIgnoreCase(naam);
        return queryListToPloegDtoList(lst);
    }

    private void checkIfPloegExists(String ploeg) throws ParameterInvalidException {
        Optional<Ploeg> foundPloeg = ploegRepository.findPloegByNaamIgnoreCase(ploeg);
        if (foundPloeg.isPresent()) {
            throw new ParameterInvalidException("Ploeg met die naam bestaat al.");
        }
    }
}
