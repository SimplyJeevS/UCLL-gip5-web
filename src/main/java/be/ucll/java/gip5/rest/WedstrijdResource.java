package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Ploeg;
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
import java.util.Optional;

@RestController
@RequestMapping("/v1/wedstrijd")
public class WedstrijdResource {
    private Logger logger = LoggerFactory.getLogger(BerichtResource.class);
    private WedstrijdRepository wedstrijdRepository;
    private PloegRepository ploegRepository;

    @Autowired
    public WedstrijdResource(WedstrijdRepository wedstrijdRepository, PloegRepository ploegRepository){
        this.wedstrijdRepository = wedstrijdRepository;
        this.ploegRepository = ploegRepository;
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Verkrijg wedstrijd",
            description = "Geef een wedstrijd ID en verkrijg de wedstrijd"
    )
    public ResponseEntity getWedstrijd(@PathVariable("id") Long id) throws ParameterInvalidException, NotFoundException {
        logger.debug("GET request voor wedstrijd gekregen");
        if(id == null && !(id instanceof Long) && id <=0 ){
            throw new ParameterInvalidException(id.toString());
        }
        Optional<Wedstrijd> wedstrijd =  wedstrijdRepository.findWedstrijdById(id);
        if(wedstrijd.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(wedstrijd.get());
        }else{
            throw new NotFoundException(id.toString());
        }
    }

    @PostMapping( value = "/")
    public ResponseEntity postWedstrijd(@RequestBody WedstrijdDTO wedstrijd) throws ParameterInvalidException, NotFoundException {
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
        Wedstrijd newWedstrijd = new Wedstrijd.WedstrijdBuilder()
                .tegenstander(wedstrijd.getTegenstander())
                .thuisPloeg(wedstrijd.getThuisPloeg())
                .locatie(wedstrijd.getLocatie())
                .tijdstip(tijdstip)
                .build();
        wedstrijdRepository.save(newWedstrijd);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWedstrijd);
    }
}
