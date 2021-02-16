package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.DeelnameRepository;
import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Deelname;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Toewijzing;
import be.ucll.java.gip5.model.Wedstrijd;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/wedstrijd")
public class WedstrijdResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private WedstrijdRepository wedstrijdRepository;
    private PloegRepository ploegRepository;
    private ToewijzingRepository toewijzingRepository;
    private DeelnameRepository deelnameRepository;

    @Autowired
    public WedstrijdResource(WedstrijdRepository wedstrijdRepository, PloegRepository ploegRepository, ToewijzingRepository toewijzingRepository, DeelnameRepository deelnameRepository){
        this.wedstrijdRepository = wedstrijdRepository;
        this.ploegRepository = ploegRepository;
        this.toewijzingRepository = toewijzingRepository;
        this.deelnameRepository = deelnameRepository;
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Verkrijg wedstrijd",
            description = "Geef een wedstrijd ID en verkrijg de wedstrijd"
    )
    public ResponseEntity getWedstrijd(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor wedstrijd gekregen");
        return ResponseEntity.status(HttpStatus.OK).body(findWedstrijdFromId(id));
    }

    @GetMapping("/")
    public ResponseEntity getWedstrijdList() throws NotFoundException {
        List<Wedstrijd> wedstrijdList = wedstrijdRepository.findAll();
        if(wedstrijdList.isEmpty()){
            throw new NotFoundException("Geen wedstrijden gevonden");
        }
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdList);
    }

    @GetMapping("/locatie/{locatie}")
    public ResponseEntity getWedstrijdFromLocatie(@PathVariable("locatie") String locatie) throws ParameterInvalidException, NotFoundException {
        if(locatie.isEmpty() || locatie.trim().length() <= 0){
            throw new ParameterInvalidException("Locatie is niet correct , kreeg "+locatie);
        }
        Optional<List<Wedstrijd>> wedstrijdList = wedstrijdRepository.findWedstrijdByLocatieContaining(locatie);
        if(!wedstrijdList.isPresent()){
            throw new NotFoundException("Geen locaties gevonden van filter "+locatie);
        }
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdList.get());
    }

    @GetMapping("/ploeg/{ploegId}")
    public ResponseEntity getWedstrijdListFromPloegId(@PathVariable("ploegId") Long ploegId) throws NotFoundException, ParameterInvalidException {
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

    @GetMapping( value = "/thuisploeg/{ploegId}")
    public ResponseEntity getWedstrijdListFromThuisploeg(@PathVariable("ploegId") Long ploegId ) throws NotFoundException, ParameterInvalidException {
        findploegFromId(ploegId);
        Optional<List<Wedstrijd>> wedstrijdList = wedstrijdRepository.findWedstrijdByThuisPloeg(ploegId);
        if(!wedstrijdList.isPresent() || wedstrijdList.get().isEmpty()){
            throw new NotFoundException("Geen wedstrijden gevonden met thuisploeg id "+ploegId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdList);
    }

    @GetMapping( value = "/tegenstander/{ploegId}")
    public ResponseEntity getWedstrijdListFromTegenstander(@PathVariable("ploegId") Long ploegId ) throws NotFoundException, ParameterInvalidException {
        findploegFromId(ploegId);
        Optional<List<Wedstrijd>> wedstrijdList = wedstrijdRepository.findWedstrijdByTegenstander(ploegId);
        if(!wedstrijdList.isPresent() || wedstrijdList.get().isEmpty()){
            throw new NotFoundException("Geen wedstrijden gevonden met tegenstander id "+ploegId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijdList);
    }

    @PutMapping( value = "/{id}/uitnodig")
    public ResponseEntity putUitnodigAlleSpelersVanThuisPloegNaarWedstrijd(@PathVariable("id") Long id,@RequestParam(value = "commentaar", required = false, defaultValue = "") String commentaar ) throws NotFoundException, ParameterInvalidException {
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

    @PostMapping( value = "/")
    public ResponseEntity postWedstrijd(@RequestBody WedstrijdDTO wedstrijd) throws ParameterInvalidException, NotFoundException {
        LocalDateTime tijdstip = checkWedstrijdDTOAndFindTijdstip(wedstrijd);
        Wedstrijd newWedstrijd = new Wedstrijd.WedstrijdBuilder()
                .tegenstander(wedstrijd.getTegenstander())
                .thuisPloeg(wedstrijd.getThuisPloeg())
                .locatie(wedstrijd.getLocatie())
                .tijdstip(tijdstip)
                .build();
        wedstrijdRepository.save(newWedstrijd);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWedstrijd);
    }

    @PutMapping( value = "/{id}")
    public ResponseEntity putWedstrijd(@PathVariable("id") Long id, @RequestBody WedstrijdDTO wedstrijd) throws NotFoundException, ParameterInvalidException {
        Wedstrijd foundWedstrijd = findWedstrijdFromId(id);
        LocalDateTime tijdstip = checkWedstrijdDTOAndFindTijdstip(wedstrijd);
        foundWedstrijd.setLocatie(wedstrijd.getLocatie());
        foundWedstrijd.setTijdstip(tijdstip);
        foundWedstrijd.setTegenstander(wedstrijd.getTegenstander());
        foundWedstrijd.setThuisPloeg(wedstrijd.getThuisPloeg());
        wedstrijdRepository.save(foundWedstrijd);
        return ResponseEntity.status(HttpStatus.OK).body(foundWedstrijd);
    }

    @DeleteMapping( value = "/{id}")
    public ResponseEntity deleteWedstrijd(@PathVariable("id") Long id) throws NotFoundException, ParameterInvalidException {
        Wedstrijd wedstrijd = findWedstrijdFromId(id);
        wedstrijdRepository.delete(wedstrijd);
        return ResponseEntity.status(HttpStatus.OK).body(wedstrijd);
    }

    private LocalDateTime checkWedstrijdDTOAndFindTijdstip(WedstrijdDTO wedstrijd) throws ParameterInvalidException, NotFoundException {
        if(wedstrijd.getLocatie().isEmpty() || wedstrijd.getLocatie().trim().length() <= 0 ){
            throw new ParameterInvalidException("Locatie met waarde "+wedstrijd.getLocatie());
        }
        LocalDateTime tijdstip;
        try {
            try {
                tijdstip = LocalDateTime.parse(wedstrijd.getTijdstip(),
                        DateTimeFormatter.ISO_INSTANT);
            } catch (Exception err) {
                tijdstip = LocalDateTime.parse(wedstrijd.getTijdstip(),
                        DateTimeFormatter.RFC_1123_DATE_TIME);
            }
        }catch(Exception err){
            throw new ParameterInvalidException("Tijdstip formaat invalid, gebruik ISO 8601 of RFC 1123/ RFC 822 formaat. tijdstip met waarde "+wedstrijd.getTijdstip());
        }
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
        return tijdstip;
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
}
