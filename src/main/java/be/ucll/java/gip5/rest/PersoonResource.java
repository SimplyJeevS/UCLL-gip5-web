package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Rol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.ucll.java.gip5.util.Api.checkApiKey;

@RestController
@RequestMapping("/rest/v1")
public class PersoonResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private PersoonRepository persoonRepository;
    private PloegRepository ploegRepository;
    private ToewijzingRepository toewijzingRepository;
    private Locale loc = new Locale("en");

    @Autowired
    public PersoonResource(PersoonRepository persoonRepository,PloegRepository ploegRepository, ToewijzingRepository toewijzingRepository){
        this.persoonRepository = persoonRepository;
        this.ploegRepository = ploegRepository;
        this.toewijzingRepository = toewijzingRepository;
    }


    /**
     * Zoek en krijg een persoon via id
     * @param id Het id van de persoon
     * @return krijg de persoon terug
     * @throws ParameterInvalidException Als de gegeven id geen correct formaat is
     * @throws NotFoundException Als er geen persoon gevonden is voor het gegeven id
     */
    @GetMapping(value = "/persoon/{id}")
    public ResponseEntity getPersoon(@PathVariable("id") Long id, @RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        logger.debug("GET request voor persoon gekregen");
        checkApiKey(api, persoonRepository);
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

    @GetMapping(value = "/persoon")
    public ResponseEntity getPersonen(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        List<Persoon> personen = persoonRepository.findAll();
        if(personen.isEmpty()){
            throw new NotFoundException("geen personen gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    /**
     * Krijg alle spelers in de database, performance van deze functie kan verbeterd worden (duurt lang nu)
     * @return krijg een list van personen terug
     * @throws NotFoundException als er geen personen gevonden zijn in de database
     */
    @GetMapping(value = "/persoon/filter")
    public ResponseEntity getPersonen(
            @RequestParam(value="voornaam", required=false) String voornaam,
            @RequestParam(value="naam", required=false) String naam,
            @RequestParam(value="geslacht", required=false) String geslacht,
            @RequestParam(value="default_rol", required = false) Rol default_rol,
            @RequestParam(value="telefoon", required = false) String telefoon,
            @RequestParam(value="adres", required=false) String adres,
            @RequestParam(value="email", required = false) String email,
            @RequestParam(value="gsm", required = false) String gsm,
            @RequestParam(name = "api", required = false, defaultValue = "") String api
    ) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        List<Persoon> personen = persoonRepository.findAll();
        List<Persoon> persoonVoornaam = Collections.emptyList();
        List<Persoon> persoonNaam = Collections.emptyList();
        List<Persoon> persoonGeslacht = Collections.emptyList();
        List<Persoon> persoonDefault_rol = Collections.emptyList();
        List<Persoon> persoonTelefoon = Collections.emptyList();
        List<Persoon> persoonAdres = Collections.emptyList();
        List<Persoon> persoonEmail = Collections.emptyList();
        List<Persoon> persoonGsm = Collections.emptyList();

        if(!(voornaam.isEmpty()||voornaam.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getVoornaam().toLowerCase().contains(voornaam.toLowerCase())){
                    persoonVoornaam.add(persoon);
                }
            });
        }
        if(!(naam.isEmpty()||naam.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getNaam().toLowerCase().contains(naam.toLowerCase())){
                    persoonNaam.add(persoon);
                }
            });
        }
        if(!(geslacht.isEmpty()||geslacht.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getGeslacht().toLowerCase().contains(geslacht.toLowerCase())){
                    persoonGeslacht.add(persoon);
                }
            });
        }
        if(!(default_rol.equals(null)||default_rol.toString().trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getDefault_rol().equals(default_rol)){
                    persoonDefault_rol.add(persoon);
                }
            });
        }
        if(!(telefoon.isEmpty()||telefoon.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getTelefoon().toLowerCase().contains(telefoon.toLowerCase())){
                    persoonTelefoon.add(persoon);
                }
            });
        }
        if(!(adres.isEmpty()||adres.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getAdres().toLowerCase().contains(adres.toLowerCase())){
                    persoonAdres.add(persoon);
                }
            });
        }
        if(!(email.isEmpty()||email.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getEmail().toLowerCase().contains(email.toLowerCase())){
                    persoonEmail.add(persoon);
                }
            });
        }
        if(!(gsm.isEmpty()||gsm.trim()=="")){
            personen.forEach(persoon -> {
                if(persoon.getGsm().toLowerCase().contains(gsm.toLowerCase())){
                    persoonGsm.add(persoon);
                }
            });
        }
        //finally create a distinct list of all list (except personen)
        List<Persoon> finalPersonen = persoonVoornaam;
        createDisctinctListFromPersonen(finalPersonen,persoonNaam);
        createDisctinctListFromPersonen(finalPersonen,persoonAdres);
        createDisctinctListFromPersonen(finalPersonen,persoonEmail);
        createDisctinctListFromPersonen(finalPersonen,persoonDefault_rol);
        createDisctinctListFromPersonen(finalPersonen,persoonGsm);
        createDisctinctListFromPersonen(finalPersonen,persoonGeslacht);
        createDisctinctListFromPersonen(finalPersonen,persoonTelefoon);

        if(finalPersonen.isEmpty()){
            throw new NotFoundException("Geen personen gevonden");
        }

        return ResponseEntity.status(HttpStatus.OK).body(finalPersonen);
    }

    /**
     * Krijg een lijst van personen op basis van voornaam
     * @param voornaam De voornaam word bekijken met een containing check (bvb: voornaam="bcd" => "abcd", "abcde", "bcdef")
     * @param ignoreCase Optioneel, is default op true. Als op false gezet word, gaat hij hoofdletters gevoelig zoeken
     * @return een lijst van personen
     * @throws ParameterInvalidException Als de voornaam of ignoreCase niet correct is meegegeven
     * @throws NotFoundException Als er geen personen zijn gevonden
     */
    @GetMapping(value = "/persoon/voornaam/{voornaam}")
    public ResponseEntity getPersonenVoornaam(@PathVariable("voornaam") String voornaam, @RequestParam(value="ignoreCase", required = false, defaultValue="true") Boolean ignoreCase, @RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(voornaam == null || voornaam.trim().length() == 0){
            throw new ParameterInvalidException("Voornaam met waarde "+voornaam);
        }
        List<Persoon> personen = new ArrayList();
        if(ignoreCase){
            personen = persoonRepository.findAllByVoornaamContaining(voornaam);
        }else{
            personen = persoonRepository.findAllByVoornaamContainingIgnoreCase(voornaam);
        }
        if(personen.isEmpty()){
            throw new NotFoundException("Personen met voornaam "+voornaam);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    /**
     * Verander de default rol van een persoon
     * @param id De id van een persoon
     * @param rol de nieuwe default rol voor de persoon
     * @return Het persoon object met nieuwe rol
     * @throws NotFoundException als de persoon niet gevonden word
     */
    @PutMapping(value="/persoon/{id}/rol")
    public ResponseEntity putDefaultPersonenNaam(@PathVariable("id") Long id, @RequestBody String rol,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        Rol nieuweDefaultRol = Rol.valueOf(rol.trim().toUpperCase());
        Optional<Persoon> persoon = persoonRepository.findPersoonById(id);
        if(!persoon.isPresent()){
            throw new NotFoundException(id.toString());
        }
        persoon.get().setDefault_rol(nieuweDefaultRol);
        persoonRepository.save(persoon.get());
        return ResponseEntity.status(HttpStatus.OK).body(persoon.get());
    }

    /**
     * Krijg een lijst van personen op basis van naam
     * @param naam De naam word bekijken met een containing check (bvb: naam="bcd" => "abcd", "abcde", "bcdef")
     * @param ignoreCase Optioneel, is default op true. Als op false gezet word, gaat hij hoofdletters gevoelig zoeken
     * @return een lijst van personen
     * @throws ParameterInvalidException Als de naam of ignoreCase niet correct is meegegeven
     * @throws NotFoundException Als er geen personen zijn gevonden
     */
    @GetMapping(value = "/persoon/naam/{naam}")
    public ResponseEntity getPersonenNaam(@PathVariable("naam") String naam, @RequestParam(value="ignoreCase", required = false, defaultValue="true") Boolean ignoreCase,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(naam == null || naam.trim().length() == 0){
            throw new ParameterInvalidException("Naam met waarde "+naam);
        }
        Optional<List<Persoon>> personen = Optional.empty();
        if(ignoreCase){
            personen = persoonRepository.findAllByNaamContainingIgnoreCase(naam);
        }else{
            personen = persoonRepository.findAllByNaamContaining(naam);
        }
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met naam "+naam);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    /**
     * Krijg een lijst van personen op basis van geslacht
     * @param geslacht Het geslacht van de persoon
     * @return Een lijst van persoon
     * @throws ParameterInvalidException Als het geslacht niet
     * @throws NotFoundException
     */
    @GetMapping(value = "/persoon/geslacht/{geslacht}")
    public ResponseEntity getPersonenGeslacht(@PathVariable("geslacht") String geslacht,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(geslacht == null || !(geslacht instanceof String)){
            throw new ParameterInvalidException("Geslacht met waarde "+geslacht);
        }
        Optional<List<Persoon>> personen = persoonRepository.findAllByGeslacht(geslacht);
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met geslacht "+geslacht);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @GetMapping(value = "/persoon/adres/{adres}")
    public ResponseEntity getPersonenAdres(@PathVariable("adres") String adres,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(adres == null || adres.trim().length() == 0){
            throw new ParameterInvalidException("Adres met waarde "+adres);
        }
        Optional<List<Persoon>> personen = persoonRepository.findAllByAdresContaining(adres);
        if(!personen.isPresent()){
            throw new NotFoundException("Personen met adres "+adres);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personen);
    }

    @PostMapping(value = "/persoon")
    public ResponseEntity postPersoon(@RequestBody PersoonDTO persoon,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        logger.debug("POST request voor persoon gekregen");
        checkPersoonInfo(persoon);
        checkPersoonWachtwoord(persoon.getWachtwoord());
        checkIfEmailIsUnique(persoon.getEmail());
        Persoon newPersoon = persoonRepository.save(
                new Persoon.PersoonBuilder()
                .adres(persoon.getAdres())
                .email(persoon.getEmail())
                .geboortedatum(persoon.getGeboortedatum())
                .geslacht(persoon.getGeslacht())
                .gsm(persoon.getGsm())
                .voornaam(persoon.getVoornaam())
                .naam(persoon.getNaam())
                .wachtwoord(persoon.getWachtwoord())
                .telefoon(persoon.getTelefoon())
                .default_rol()
                .api()
                .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newPersoon);
    }


    @PutMapping (value = "/persoon/{id}")
    public ResponseEntity putPersoon(@PathVariable("id") Long id, @RequestBody PersoonDTO persoon,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        checkPersoonInfo(persoon);
        Optional<Persoon> foundPersoon = persoonRepository.findPersoonById(id);
        if(!foundPersoon.isPresent()){
            throw new NotFoundException("Persoon met id "+id);
        }
        Persoon updatedPersoon = foundPersoon.get();
        updatedPersoon.setAdres(persoon.getAdres());
        updatedPersoon.setEmail(persoon.getEmail());
        updatedPersoon.setGeboortedatum(persoon.getGeboortedatum());
        updatedPersoon.setGeslacht(persoon.getGeslacht());
        updatedPersoon.setGsm(persoon.getGsm());
        updatedPersoon.setNaam(persoon.getNaam());
        updatedPersoon.setVoornaam(persoon.getVoornaam());
        persoonRepository.save(updatedPersoon);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPersoon);
    }

    @PutMapping (value = "/persoon/{id}/wachtwoord")
    public ResponseEntity putPersoonWachtwoord(@PathVariable("id") Long id, @RequestBody String wachtwoord,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
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

    @PutMapping (value = "/persoon/{id}/info")
    public ResponseEntity putPersoonPloegId(@PathVariable("id") Long id, @RequestBody PersoonDTO persoon,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        if(id == null || !(id instanceof Long) || id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Persoon> foundPersoon = persoonRepository.findPersoonById(id);
        if(!foundPersoon.isPresent()){
            throw new NotFoundException("Persoon met id "+id);
        }
        checkPersoonInfo(persoon);
        Persoon updatedPersoon = foundPersoon.get();
        updatedPersoon.setAdres(persoon.getAdres());
        updatedPersoon.setEmail(persoon.getEmail());
        updatedPersoon.setGeboortedatum(persoon.getGeboortedatum());
        updatedPersoon.setGeslacht(persoon.getGeslacht());
        updatedPersoon.setGsm(persoon.getGsm());
        updatedPersoon.setNaam(persoon.getNaam());
        updatedPersoon.setVoornaam(persoon.getVoornaam());
        persoonRepository.save(updatedPersoon);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPersoon);
    }

    @DeleteMapping(value = "/persoon/{id}")
    public ResponseEntity deletePersoon(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
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

    private void checkPersoonInfo(PersoonDTO persoon) throws ParameterInvalidException {
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
//        if(persoon.getGeboortedatum() == null || persoon.getGeboortedatum().trim().length() == 0){
//            throw new ParameterInvalidException("Geboortedatum met waarde "+persoon.getGeboortedatum());
//        }
//        Date geboortedatum;
//        try {
//            geboortedatum = new SimpleDateFormat("dd/MM/yyyy").parse(persoon.getGeboortedatum());
//        }catch(Exception err){
//            throw new ParameterInvalidException("Geboorte datum formaat invalid, gebruik dd/MM/yyyy formaat (vb: 31/12/2020). Geboortedatum met waarde "+persoon.getGeboortedatum());
//        }
        if(persoon.getGeboortedatum().after(new Date())){
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

        return;
    }
    private void checkPersoonWachtwoord(String wachtwoord) throws ParameterInvalidException {
        if(wachtwoord == null || wachtwoord.trim().length() < 8){
            throw new ParameterInvalidException("Wachtwoord moet minstens 8 characters bevatten, u gaf "+wachtwoord);
        }
    }
    private void createDisctinctListFromPersonen(List<Persoon> finalList, List<Persoon> addingList){
        addingList.forEach(persoon -> {
            AtomicReference<Boolean> notFoundAdding = new AtomicReference<>(true);
            addingList.forEach(f-> {
                if(f.getId()==persoon.getId()){
                    notFoundAdding.set(false);
                }
            });
            if(notFoundAdding.get()){
                finalList.add(persoon);
            }
        });
    }

    public void setLocale(Locale loc) {this.loc = loc;
    }
    private List<PersoonDTO> queryListToPersoonDtoList(List<Persoon> lst){
        Stream<PersoonDTO> stream = lst.stream()
                .map(rec -> {
                    PersoonDTO dto = new PersoonDTO();
                    dto.setAdres(rec.getAdres());
                    dto.setGeslacht(rec.getGeslacht());
                    dto.setDefaultRol(rec.getDefault_rol());
                    dto.setGeboortedatum(rec.getGeboortedatum());
                    dto.setEmail(rec.getEmail());
                    dto.setNaam(rec.getNaam());
                    dto.setWachtwoord(rec.getWachtwoord());
                    dto.setVoornaam(rec.getVoornaam());
                    dto.setTelefoon(rec.getTelefoon());
                    dto.setGsm(rec.getGsm());
                    return dto;
                });
        return stream.collect(Collectors.toList());
    }
    public List<PersoonDTO> getAllPersonen() {return queryListToPersoonDtoList(persoonRepository.findAll());}
    public List<PersoonDTO> getSearchPersonen(String naam) throws IllegalArgumentException {
        if (naam == null || naam.trim().length() == 0)
            throw new IllegalArgumentException("Personen ophalen met de naam gefaald. Naam leeg");

        List<Persoon> lst = persoonRepository.findAllByVoornaamContaining(naam);
        return queryListToPersoonDtoList(lst);
    }
    private void checkIfEmailIsUnique(String email) throws ParameterInvalidException {
        Optional<Persoon> persoon = persoonRepository.findPersoonByEmailIgnoreCase(email);
        if(persoon.isPresent()){
            throw new ParameterInvalidException("Email bestaat al");
        }
    }
}
