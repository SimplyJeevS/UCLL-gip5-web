package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.*;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.dto.WedstrijdListDTO;
import be.ucll.java.gip5.dto.WedstrijdMetPloegenDTO;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.ucll.java.gip5.util.Api.checkApiKey;

@RestController
@RequestMapping("/rest/v1")
public class WedstrijdResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private WedstrijdRepository wedstrijdRepository;
    private PloegRepository ploegRepository;
    private ToewijzingRepository toewijzingRepository;
    private DeelnameRepository deelnameRepository;
    private PersoonRepository persoonRepository;
    private Locale loc = new Locale("en");

    @Autowired
    public WedstrijdResource(WedstrijdRepository wedstrijdRepository, PloegRepository ploegRepository, ToewijzingRepository toewijzingRepository, DeelnameRepository deelnameRepository, PersoonRepository persoonRepository){
        this.wedstrijdRepository = wedstrijdRepository;
        this.ploegRepository = ploegRepository;
        this.toewijzingRepository = toewijzingRepository;
        this.deelnameRepository = deelnameRepository;
        this.persoonRepository = persoonRepository;
    }

    @GetMapping(value = "/wedstrijd/{id}")
    public ResponseEntity getWedstrijd(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
         checkApiKey(api,persoonRepository);
        logger.debug("GET request voor wedstrijd gekregen");
        return ResponseEntity.status(HttpStatus.OK).body(findWedstrijdFromId(id));
    }

    @GetMapping(value = "/wedstrijdMetPloegen/{id}")
    public ResponseEntity getWedstrijdMetPLoegen(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        logger.debug("GET request voor wedstrijd gekregen");
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(id);
        if(!wedstrijd.isPresent()){
            throw new NotFoundException("Geen wedstrijden gevonden met de meegegeven id");
        }
        WedstrijdMetPloegenDTO wedstrijdMetPloegenDTO = new WedstrijdMetPloegenDTO(id,wedstrijd.get().getTijdstip(),wedstrijd.get().getLocatie(),wedstrijd.get().getThuisPloeg(),wedstrijd.get().getTegenstander(),null,null);
        Optional<Ploeg> thuisploeg = ploegRepository.findPloegById(wedstrijd.get().getThuisPloeg());
        Optional<Ploeg> tegenstander = ploegRepository.findPloegById(wedstrijd.get().getTegenstander());
        wedstrijdMetPloegenDTO.setThuisploeg((thuisploeg.isPresent())?thuisploeg.get().getNaam():"Geen thuisploeg gevonden");
        wedstrijdMetPloegenDTO.setTegenstander((tegenstander.isPresent())?tegenstander.get().getNaam():"Geen tegenstander gevonden");
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdMetPloegenDTO);
    }


    @GetMapping("/wedstrijdMetPloegen")
    public List<WedstrijdMetPloegenDTO> getWedstrijdMetPLoegenVaadin(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws InvalidCredentialsException, NotFoundException {
        Persoon persoon = checkApiKey(api,persoonRepository);
        List<WedstrijdMetPloegenDTO> wedstrijdMetPloegenDTOList = new ArrayList<>();
        if(persoon.getDefault_rol().equals(Rol.SECRETARIS)){
            List<Wedstrijd> wedstrijdList = wedstrijdRepository.findAll();
            for (Wedstrijd w : wedstrijdList) {
                Optional<Ploeg> tegenstander = ploegRepository.findPloegById(w.getTegenstander());
                Optional<Ploeg> thuisploeg = ploegRepository.findPloegById(w.getThuisPloeg());
                wedstrijdMetPloegenDTOList.add(new WedstrijdMetPloegenDTO(w.getId(), w.getTijdstip(), w.getLocatie(),w.getThuisPloeg(), w.getTegenstander(),( (tegenstander.isPresent())?tegenstander.get().getNaam():"Geen thuisploeg gevonden"), (thuisploeg.isPresent())?thuisploeg.get().getNaam():"Geen thuisploeg gevonden"));
            }
        }else {
            Long id = persoon.getId();
            Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(id);
            if (!toewijzingList.isPresent()) {
                throw new NotFoundException("Deze persoon heeft geen toewijzing(en)");
            }
            List<Ploeg> ploegList = new ArrayList<>();
            for (Toewijzing t : toewijzingList.get()) {
                Optional<Ploeg> ploeg = ploegRepository.findPloegById(t.getPloegId());
                if (ploeg.isPresent()) {
                    ploegList.add(ploeg.get());
                }
            }
            for (Ploeg p : ploegList) {
                Optional<List<Wedstrijd>> wedstrijd = wedstrijdRepository.findWedstrijdByThuisPloeg(p.getId());
                if (wedstrijd.isPresent()) {
                    for (Wedstrijd w : wedstrijd.get()) {
                        Optional<Ploeg> tegenstander = ploegRepository.findPloegById(w.getTegenstander());
                        wedstrijdMetPloegenDTOList.add(new WedstrijdMetPloegenDTO(w.getId(), w.getTijdstip(), w.getLocatie(), p.getId(), w.getTegenstander(), p.getNaam(), tegenstander.get().getNaam()));
                    }
                }
            }
        }
        List<WedstrijdMetPloegenDTO> finalWedstrijdMetPloegenDTOList = new ArrayList<>();
        for (WedstrijdMetPloegenDTO w : wedstrijdMetPloegenDTOList) {
            if(w.getTijdstip().isAfter(LocalDateTime.now().minusDays(1))){
                finalWedstrijdMetPloegenDTOList.add(w);
            }
        }
        return finalWedstrijdMetPloegenDTOList;
    }

    public List<WedstrijdMetPloegenDTO> GetWedstrijdSearchVaadin(String searchTerm, @RequestParam(name = "api", required = false, defaultValue = "") String api) throws InvalidCredentialsException, NotFoundException {
        Persoon persoon = checkApiKey(api,persoonRepository);
        List<WedstrijdMetPloegenDTO> wedstrijdMetPloegenDTOList = new ArrayList<>();
        if(persoon.getDefault_rol().equals(Rol.SECRETARIS)){
            List<Wedstrijd> wedstrijdList = wedstrijdRepository.findAllByLocatieContainingIgnoreCase(searchTerm);
            for (Wedstrijd w : wedstrijdList) {
                Optional<Ploeg> tegenstander = ploegRepository.findPloegById(w.getTegenstander());
                Optional<Ploeg> thuisploeg = ploegRepository.findPloegById(w.getThuisPloeg());
                wedstrijdMetPloegenDTOList.add(new WedstrijdMetPloegenDTO(w.getId(), w.getTijdstip(), w.getLocatie(),w.getThuisPloeg(), w.getTegenstander(),( (tegenstander.isPresent())?tegenstander.get().getNaam():"Geen thuisploeg gevonden"), (thuisploeg.isPresent())?thuisploeg.get().getNaam():"Geen thuisploeg gevonden"));
            }
        }else {
            Long id = persoon.getId();
            Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(id);
            if (!toewijzingList.isPresent()) {
                throw new NotFoundException("Deze persoon heeft geen toewijzing(en)");
            }
            List<Ploeg> ploegList = new ArrayList<>();
            for (Toewijzing t : toewijzingList.get()) {
                Optional<Ploeg> ploeg = ploegRepository.findPloegById(t.getPloegId());
                if (ploeg.isPresent()) {
                    ploegList.add(ploeg.get());
                }
            }
            for (Ploeg p : ploegList) {
                Optional<List<Wedstrijd>> wedstrijd = wedstrijdRepository.findWedstrijdByThuisPloegAndLocatieContainingIgnoreCase(p.getId(), searchTerm);
                if (wedstrijd.isPresent()) {
                    for (Wedstrijd w : wedstrijd.get()) {
                        Optional<Ploeg> tegenstander = ploegRepository.findPloegById(w.getTegenstander());
                        wedstrijdMetPloegenDTOList.add(new WedstrijdMetPloegenDTO(w.getId(), w.getTijdstip(), w.getLocatie(), p.getId(), w.getTegenstander(), p.getNaam(), tegenstander.get().getNaam()));
                    }
                }
            }
        }
        List<WedstrijdMetPloegenDTO> finalWedstrijdMetPloegenDTOList = new ArrayList<>();
        for (WedstrijdMetPloegenDTO w : wedstrijdMetPloegenDTOList) {
            if(w.getTijdstip().isAfter(LocalDateTime.now().minusDays(1))){
                finalWedstrijdMetPloegenDTOList.add(w);
            }
        }
        return finalWedstrijdMetPloegenDTOList;
    }

    @GetMapping("/wedstrijd")
    public ResponseEntity getWedstrijdList(@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, InvalidCredentialsException {
        Persoon persoon = checkApiKey(api,persoonRepository);
        List<Wedstrijd> wedstrijdList = new ArrayList<>();
        if(persoon.getDefault_rol().equals(Rol.SECRETARIS)){
            wedstrijdList = wedstrijdRepository.findAll();
        }else {
            Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPersoonId(persoon.getId());
            if (!toewijzingList.isPresent()) {
                throw new NotFoundException("Deze persoon heeft geen toewijzing(en)");
            }
            List<Ploeg> ploegList = new ArrayList<>();
            for (Toewijzing t : toewijzingList.get()) {
                Optional<Ploeg> ploeg = ploegRepository.findPloegById(t.getPloegId());
                if (ploeg.isPresent()) {
                    ploegList.add(ploeg.get());
                }
            }
            for (Ploeg p : ploegList) {
                Optional<List<Wedstrijd>> wedstrijd = wedstrijdRepository.findWedstrijdByThuisPloeg(p.getId());
                if (wedstrijd.isPresent()) {
                    for (Wedstrijd w : wedstrijd.get()) {
                        Optional<Ploeg> tegenstander = ploegRepository.findPloegById(w.getTegenstander());
                        wedstrijdList.add(w);
                    }
                }
            }
        }
        if(wedstrijdList.isEmpty()){
            throw new NotFoundException("Geen wedstrijden gevonden");
        }
        List<Wedstrijd> finalWedstrijdList = new ArrayList<>();
        for (Wedstrijd w : wedstrijdList) {
            if(w.getTijdstip().isAfter(LocalDateTime.now().minusDays(1))){
                finalWedstrijdList.add(w);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(finalWedstrijdList);
    }

    //tijdstip

    /**
     *
     * (zone='na/voor/tussen', tijdstip="tijd" )
     */

    @GetMapping("/wedstrijd/locatie/{locatie}")
    public ResponseEntity getWedstrijdFromLocatie(@PathVariable("locatie") String locatie,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        //check de rol => secre, mag alles bekijken anders hun wedstrijden
        if(locatie.isEmpty() || locatie.trim().length() <= 0){
            throw new ParameterInvalidException("Locatie is niet correct , kreeg "+locatie);
        }
        Optional<List<Wedstrijd>> wedstrijdList = wedstrijdRepository.findWedstrijdByLocatieContaining(locatie);
        if(!wedstrijdList.isPresent()){
            throw new NotFoundException("Geen locaties gevonden van filter "+locatie);
        }
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdList.get());
    }

    @GetMapping("/wedstrijd/ploeg/{ploegId}")
    public ResponseEntity getWedstrijdListFromPloegId(@PathVariable("ploegId") Long ploegId,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        findploegFromId(ploegId);
        Optional<List<Wedstrijd>> alsThuisploeg = wedstrijdRepository.findWedstrijdByThuisPloeg(ploegId);
        Optional<List<Wedstrijd>> alsTegenstander = wedstrijdRepository.findWedstrijdByTegenstander(ploegId);
        List<Wedstrijd> wedstrijdList = Collections.emptyList();
        wedstrijdList.addAll(alsTegenstander.get());
        wedstrijdList.addAll(alsThuisploeg.get());
        List<Wedstrijd> uniekWedstrijdList = wedstrijdList.stream()
                .distinct()
                .collect(Collectors.toList());
        if(uniekWedstrijdList.isEmpty()){
            throw new NotFoundException("Geen ploegen gevonden met id "+ploegId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(uniekWedstrijdList);
    }

    @GetMapping( value = "/wedstrijd/thuisploeg/{ploegId}")
    public ResponseEntity getWedstrijdListFromThuisploeg(@PathVariable("ploegId") Long ploegId ,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        findploegFromId(ploegId);
        Optional<List<Wedstrijd>> wedstrijdList = wedstrijdRepository.findWedstrijdByThuisPloeg(ploegId);
        if(!wedstrijdList.isPresent() || wedstrijdList.get().isEmpty()){
            throw new NotFoundException("Geen wedstrijden gevonden met thuisploeg id "+ploegId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdList);
    }

    @GetMapping( value = "/wedstrijd/tegenstander/{ploegId}")
    public ResponseEntity getWedstrijdListFromTegenstander(@PathVariable("ploegId") Long ploegId,@RequestParam(name = "api", required = false, defaultValue = "") String api ) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        findploegFromId(ploegId);
        Optional<List<Wedstrijd>> wedstrijdList = wedstrijdRepository.findWedstrijdByTegenstander(ploegId);
        if(!wedstrijdList.isPresent() || wedstrijdList.get().isEmpty()){
            throw new NotFoundException("Geen wedstrijden gevonden met tegenstander id "+ploegId);
        }
        List<Wedstrijd> finalWedstrijdList = new ArrayList<>();
        for (Wedstrijd w : wedstrijdList.get()) {
            if(w.getTijdstip().isAfter(LocalDateTime.now().minusDays(1))){
                finalWedstrijdList.add(w);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(finalWedstrijdList);
    }

    @PutMapping( value = "/wedstrijd/{id}/uitnodig")
    public ResponseEntity putUitnodigAlleSpelersVanThuisPloegNaarWedstrijd(@PathVariable("id") Long id,@RequestParam(value = "commentaar", required = false, defaultValue = "") String commentaar ,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        Wedstrijd wedstrijd = findWedstrijdFromId(id);
        findploegFromId(wedstrijd.getThuisPloeg());
        Optional<List<Toewijzing>> toewijzingList = toewijzingRepository.findAllByPloegId(wedstrijd.getThuisPloeg());
        if(!toewijzingList.isPresent() || toewijzingList.get().isEmpty()){
            throw new NotFoundException("Geen toewijzingen gevonden voor de ploeg met id "+wedstrijd.getThuisPloeg());
        }
        List<Deelname> deelnameList = Collections.emptyList();
        toewijzingList.get().forEach(toewijzing -> {
            Deelname newDeelname = new Deelname.DeelnameBuilder()
                    .wedstrijdId(id)
                    .persoonId(toewijzing.getPersoonId())
                    .commentaar(commentaar)
                    .build();
            deelnameList.add(newDeelname);
        });
        deelnameRepository.saveAll(deelnameList);
        return ResponseEntity.status(HttpStatus.OK).body(deelnameList);
    }

    @PostMapping( value = "/wedstrijd")
    public ResponseEntity postWedstrijd(@RequestBody WedstrijdDTO wedstrijd,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        checkWedstrijdDTOAndFindTijdstip(wedstrijd);
        Wedstrijd newWedstrijd = new Wedstrijd.WedstrijdBuilder()
                .tegenstander(wedstrijd.getTegenstander())
                .thuisPloeg(wedstrijd.getThuisPloeg())
                .locatie(wedstrijd.getLocatie())
                .tijdstip(wedstrijd.getTijdstip())
                .build();
        wedstrijdRepository.save(newWedstrijd);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWedstrijd);
    }

    @PutMapping( value = "/wedstrijd/{id}")
    public ResponseEntity putWedstrijd(@PathVariable("id") Long id, @RequestBody WedstrijdDTO wedstrijd,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        Optional<Wedstrijd> foundWedstrijd = wedstrijdRepository.findWedstrijdById(id);
        checkWedstrijdDTOAndFindTijdstip(wedstrijd);
        foundWedstrijd.get().setLocatie(wedstrijd.getLocatie());
        foundWedstrijd.get().setTijdstip(wedstrijd.getTijdstip());
        foundWedstrijd.get().setTegenstander(wedstrijd.getTegenstander());
        foundWedstrijd.get().setThuisPloeg(wedstrijd.getThuisPloeg());
        wedstrijdRepository.save(foundWedstrijd.get());
        return ResponseEntity.status(HttpStatus.OK).body(foundWedstrijd);
    }

    @DeleteMapping( value = "/wedstrijd/{id}")
    public ResponseEntity deleteWedstrijd(@PathVariable("id") Long id,@RequestParam(name = "api", required = false, defaultValue = "") String api) throws NotFoundException, ParameterInvalidException, InvalidCredentialsException {
        checkApiKey(api,persoonRepository);
        Optional<Wedstrijd> wedstrijd = wedstrijdRepository.findWedstrijdById(id);
        wedstrijdRepository.delete(wedstrijd.get());
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijd);
    }

    private void checkWedstrijdDTOAndFindTijdstip(WedstrijdDTO wedstrijd) throws ParameterInvalidException, NotFoundException {
        if(wedstrijd.getLocatie().isEmpty() || wedstrijd.getLocatie().trim().length() <= 0 ){
            throw new ParameterInvalidException("Locatie met waarde "+wedstrijd.getLocatie());
        }
        LocalDateTime tijdstip;
//        try {
//            try {
//                tijdstip = LocalDateTime.parse(wedstrijd.getTijdstip(),
//                        DateTimeFormatter.ISO_INSTANT);
//            } catch (Exception err) {
//                tijdstip = LocalDateTime.parse(wedstrijd.getTijdstip(),
//                        DateTimeFormatter.RFC_1123_DATE_TIME);
//            }
//        }catch(Exception err){
//            throw new ParameterInvalidException("Tijdstip formaat invalid, gebruik ISO 8601 of RFC 1123/ RFC 822 formaat. tijdstip met waarde "+wedstrijd.getTijdstip());
//        }
        if(!(wedstrijd.getTegenstander() instanceof Long) || wedstrijd.getTegenstander() <=0){
            throw new ParameterInvalidException("Tegenstander moet een postitief nummer zijn");
        }
        if(!(wedstrijd.getThuisPloeg() instanceof Long) || wedstrijd.getThuisPloeg() <=0){
            throw new ParameterInvalidException("Thuisploeg moet een postitief nummer zijn");
        }
        Optional<Ploeg> thuisploeg = ploegRepository.findPloegById(wedstrijd.getThuisPloeg());
        Optional<Ploeg> tegenstander = ploegRepository.findPloegById(wedstrijd.getTegenstander());
        if(!thuisploeg.isPresent()){
            throw new NotFoundException("Thuisploeg met id "+wedstrijd.getThuisPloeg());
        }
        if(!tegenstander.isPresent()){
            throw new NotFoundException("Tegenstander met id "+wedstrijd.getThuisPloeg());
        }
    }
    private Wedstrijd findWedstrijdFromId(Long id) throws ParameterInvalidException, NotFoundException {
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Wedstrijd> wedstrijd =  wedstrijdRepository.findWedstrijdById(id);
        if(wedstrijd.isPresent()){
            return wedstrijd.get();
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    private Ploeg findploegFromId(Long id) throws ParameterInvalidException, NotFoundException {
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Ploeg> ploeg =  ploegRepository.findPloegById(id);
        if(ploeg.isPresent()){
            return ploeg.get();
        }else{
            throw new NotFoundException(id.toString());
        }
    }
    public void setLocale(Locale loc) {this.loc = loc;
    }
    private List<WedstrijdMetPloegenDTO> queryListToWedstrijdMetPloegenDTOList(List<Wedstrijd> lst){
        Stream< WedstrijdMetPloegenDTO> stream = lst.stream()
                .map(rec -> {
                    WedstrijdMetPloegenDTO dto = new  WedstrijdMetPloegenDTO();
                    dto.setId(rec.getId());
                    dto.setLocatie(rec.getLocatie());
                    dto.setTegenstanderId(rec.getTegenstander());
                    dto.setThuisploegId(rec.getThuisPloeg());
                    Optional<Ploeg> thuisploeg = ploegRepository.findPloegById(rec.getThuisPloeg());
                    Optional<Ploeg> tegenstander = ploegRepository.findPloegById(rec.getTegenstander());
                    dto.setThuisploeg((thuisploeg.isPresent())?thuisploeg.get().getNaam():"Geen thuisploeg gevonden");
                    dto.setTegenstander((tegenstander.isPresent())?tegenstander.get().getNaam():"Geen tegenstander gevonden");
                    dto.setTijdstip((rec.getTijdstip()));
                    return dto;
                });
        return stream.collect(Collectors.toList());
    }
    public List< WedstrijdMetPloegenDTO> getAllWedstrijden() {
        return queryListToWedstrijdMetPloegenDTOList(wedstrijdRepository.findAll());
    }
    public List< WedstrijdMetPloegenDTO> getSearchWedstrijden(String locatie) throws IllegalArgumentException {
        if (locatie == null || locatie.trim().length() == 0)
            throw new IllegalArgumentException("Wedstrijden ophalen met de locatie gefaald. locatie leeg");

        List<Wedstrijd> lst = wedstrijdRepository.findAllByLocatieContainingIgnoreCase(locatie);
        return queryListToWedstrijdMetPloegenDTOList(lst);
    }
}
