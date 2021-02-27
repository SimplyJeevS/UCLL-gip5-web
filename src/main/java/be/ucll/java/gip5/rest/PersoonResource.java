package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Rol;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/persoon")
public class PersoonResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PersoonRepository persoonRepository;
    private PloegRepository ploegRepository;
    private ToewijzingRepository toewijzingRepository;

    @Autowired
    public PersoonResource(PersoonRepository persoonRepository,PloegRepository ploegRepository, ToewijzingRepository toewijzingRepository){
        this.persoonRepository = persoonRepository;
        this.ploegRepository = ploegRepository;
        this.toewijzingRepository = toewijzingRepository;
    }



    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Verkrijg persoon",
            description = "Geef een persoon ID en verkrijg de persoon"
    )
    public ResponseEntity getPersoon(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor persoon gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> persoon =  persoonRepository.findPersoonById(id);
        if(persoon.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(persoon.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    @GetMapping(value = "/")
    @Operation(
            summary = "Verkrijg alle personen",
            description = "Krijg een array van alle personen in de database"
    )
    public ResponseEntity getPersonen() throws NotFoundException {
        List<Persoon> personen = persoonRepository.findAll();
        if(personen.isEmpty()){
            throw new NotFoundException("Personen ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @GetMapping(value = "/voornaam/{voornaam}")
    @Operation(
            summary = "Verkrijg alle personen met voornaam",
            description = "Krijg een array van alle personen in de database die een specifieke voornaam bevatten"
    )
    public ResponseEntity getPersonenVoornaam(@PathVariable("voornaam") String voornaam) throws ParameterInvalidException, NotFoundException {
        if(voornaam == null || voornaam.trim().length() == 0){
            throw new ParameterInvalidException("Voornaam met waarde "+voornaam);
        }
        Optional<List<Persoon>> personen = persoonRepository.findAllByVoornaamContaining(voornaam);
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met voornaam "+voornaam);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @GetMapping(value = "/naam/{naam}")
    @Operation(
            summary = "Verkrijg alle personen met naam",
            description = "Krijg een array van alle personen in de database die een specifieke naam bevatten"
    )
    public ResponseEntity getPersonenNaam(@PathVariable("naam") String naam) throws ParameterInvalidException, NotFoundException {
        if(naam == null || naam.trim().length() == 0){
            throw new ParameterInvalidException("Naam met waarde "+naam);
        }
        Optional<List<Persoon>> personen = persoonRepository.findAllByNaamContaining(naam);
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met naam "+naam);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @GetMapping(value = "/geslacht/{geslacht}")
    @Operation(
            summary = "Verkrijg alle personen met geslacht",
            description = "Krijg een array van alle personen in de database die een van het gegeven geslacht zijn"
    )
    public ResponseEntity getPersonenGeslacht(@PathVariable("geslacht") Boolean geslacht) throws ParameterInvalidException, NotFoundException {
        if(geslacht == null || !(geslacht instanceof Boolean)){
            throw new ParameterInvalidException("Geslacht met waarde "+geslacht);
        }
        Optional<List<Persoon>> personen = persoonRepository.findAllByGeslacht(geslacht);
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met geslacht "+geslacht);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @GetMapping(value = "/adres/{adres}")
    @Operation(
            summary = "Verkrijg alle personen met adres",
            description = "Krijg een array van alle personen in de database die een specifiek adres bevatten"
    )
    public ResponseEntity getPersonenAdres(@PathVariable("adres") String adres) throws ParameterInvalidException, NotFoundException {
        if(adres == null || adres.trim().length() == 0){
            throw new ParameterInvalidException("Adres met waarde "+adres);
        }
        Optional<List<Persoon>> personen = persoonRepository.findAllByAdresContaining(adres);
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met adres "+adres);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @PostMapping(value = "/")
    @Operation(
            summary = "Maak een persoon",
            description = "Creeer een nieuwe persoon, info + wachtwoord"
    )
    public ResponseEntity postPersoon(@PathVariable PersoonDTO persoon) throws ParameterInvalidException, NotFoundException {
        logger.debug("POST request voor persoon gekregen");
        Date geboortedatum = checkPersoonInfo(persoon);
        checkPersoonWachtwoord(persoon.getWachtwoord());
        Persoon newPersoon = persoonRepository.save(
                new Persoon.PersoonBuilder()
                .adres(persoon.getAdres())
                .email(persoon.getEmail())
                .geboortedatum(geboortedatum)
                .geslacht(persoon.getGeslacht())
                .gsm(persoon.getGsm())
                .voornaam(persoon.getVoornaam())
                .naam(persoon.getNaam())
                .wachtwoord(persoon.getWachtwoord())
                .telefoon(persoon.getTelefoon())
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newPersoon);
    }

    @PutMapping (value = "/{id}")
    @Operation(
            summary = "Pas persoon aan",
            description = "verander het hele persoons object (info + ploegId)"
    )
    public ResponseEntity putPersoon(@PathVariable("id") Long id, @RequestBody PersoonDTO persoon) throws ParameterInvalidException, NotFoundException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Date geboortedatum = checkPersoonInfo(persoon);
        Optional<Persoon> foundPersoon = persoonRepository.findPersoonById(id);
        if(!foundPersoon.isPresent()){
            throw new NotFoundException("Persoon met id "+id);
        }
        Persoon updatedPersoon = foundPersoon.get();
        updatedPersoon.setAdres(persoon.getAdres());
        updatedPersoon.setEmail(persoon.getEmail());
        updatedPersoon.setGeboortedatum(geboortedatum);
        updatedPersoon.setGeslacht(persoon.getGeslacht());
        updatedPersoon.setGsm(persoon.getGsm());
        updatedPersoon.setNaam(persoon.getNaam());
        updatedPersoon.setVoornaam(persoon.getVoornaam());
        persoonRepository.save(updatedPersoon);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPersoon);
    }

    @PutMapping (value = "/{id}/wachtwoord")
    @Operation(
            summary = "Reset wachtwoord",
            description = "Reset een wachtwoord van een persoon"
    )
    public ResponseEntity putPersoonWachtwoord(@PathVariable("id") Long id, @RequestBody String wachtwoord) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> foundPersoon = persoonRepository.findPersoonById(id);
        if(!foundPersoon.isPresent()){
            throw new NotFoundException("Persoon met id "+id);
        }
        checkPersoonWachtwoord(wachtwoord);
        foundPersoon.get().setWachtwoord(wachtwoord);
        persoonRepository.save(foundPersoon.get());
        return ResponseEntity.status(HttpStatus.OK).body(foundPersoon);
    }

    @PutMapping (value = "/{id}/info")
    @Operation(
            summary = "Verander info",
            description = "Verander algemene informatie van een persoon"
    )
    public ResponseEntity putPersoonPloegId(@PathVariable("id") Long id, @RequestBody PersoonDTO persoon) throws NotFoundException, ParameterInvalidException {
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> foundPersoon = persoonRepository.findPersoonById(id);
        if(!foundPersoon.isPresent()){
            throw new NotFoundException("Persoon met id "+id);
        }
        Date geboortedatum = checkPersoonInfo(persoon);
        Persoon updatedPersoon = foundPersoon.get();
        updatedPersoon.setAdres(persoon.getAdres());
        updatedPersoon.setEmail(persoon.getEmail());
        updatedPersoon.setGeboortedatum(geboortedatum);
        updatedPersoon.setGeslacht(persoon.getGeslacht());
        updatedPersoon.setGsm(persoon.getGsm());
        updatedPersoon.setNaam(persoon.getNaam());
        updatedPersoon.setVoornaam(persoon.getVoornaam());
        persoonRepository.save(updatedPersoon);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPersoon);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Verwijder een persoon",
            description = "Geef het id van een persoon mee om het te verwijderen"
    )
    public ResponseEntity deletePersoon(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonById(id);
        if(!persoon.isPresent()){
            throw new NotFoundException("Persoon met id "+id);
        }
        persoonRepository.delete(persoon.get());
        return ResponseEntity.status(HttpStatus.OK).body(persoon.get());
    }

    private Date checkPersoonInfo(PersoonDTO persoon) throws ParameterInvalidException {
        if(persoon.getGeslacht().isEmpty() || persoon.getGeslacht().trim().length() <= 0){
            throw new ParameterInvalidException("Geslacht met waarde "+persoon.getGeslacht());
        }else if(persoon.getGeslacht().trim().length() != 1){
            throw new ParameterInvalidException("Geslacht moet exact 1 character hebben (V/M), kreeg waarde "+persoon.getGeslacht());
        }
        if(persoon.getAdres() == null || persoon.getAdres().trim().length() == 0){
            throw new ParameterInvalidException("Adress met waarde "+persoon.getAdres());
        }
        if(persoon.getEmail() == null || persoon.getEmail().trim().length() == 0){
            throw new ParameterInvalidException("E-mail met waarde "+persoon.getEmail());
        }
        if(persoon.getGeboortedatum() == null || persoon.getGeboortedatum().trim().length() == 0){
            throw new ParameterInvalidException("Geboortedatum met waarde "+persoon.getGeboortedatum());
        }
        Date geboortedatum;
        try {
            geboortedatum = new SimpleDateFormat("dd/MM/yyyy").parse(persoon.getGeboortedatum());
        }catch(Exception err){
            throw new ParameterInvalidException("Geboorte datum formaat invalid, gebruik dd/MM/yyyy formaat (vb: 31/12/2020). Geboortedatum met waarde "+persoon.getGeboortedatum());
        }
        if(geboortedatum.after(new Date())){
            throw new ParameterInvalidException("Geboortedatum ligt niet in het verleden, "+persoon.getGeboortedatum());
        }
        if(persoon.getGsm() == null || persoon.getGsm().trim().length() == 0){
            throw new ParameterInvalidException("Gsm met waarde "+persoon.getGsm());
        }
        if(persoon.getNaam() == null || persoon.getNaam().trim().length() == 0){
            throw new ParameterInvalidException("Naam met waarde "+persoon.getNaam());
        }
        if(persoon.getTelefoon() == null || persoon.getTelefoon().trim().length() == 0){
            throw new ParameterInvalidException("Telefoon met waarde "+persoon.getTelefoon());
        }

        return geboortedatum;
    }
    private void checkPersoonWachtwoord(String wachtwoord) throws ParameterInvalidException {

        if(wachtwoord == null || wachtwoord.trim().length() < 8){
            throw new ParameterInvalidException("Wachtwoord moet minstens 8 characters bevatten, u gaf "+wachtwoord);
        }
    }
}
