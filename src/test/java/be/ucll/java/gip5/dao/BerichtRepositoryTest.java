package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.AbstractIntegrationTest;
import be.ucll.java.gip5.model.Bericht;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Wedstrijd;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class BerichtRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    BerichtRepository berichtRepository;
    @Autowired
    WedstrijdRepository wedstrijdRepository;
    @Autowired
    PersoonRepository persoonRepository;
    @Autowired
    PloegRepository ploegRepository;

    private Persoon testPersoon;
    private Wedstrijd testWedstrijd;
    private Ploeg testploeg1,testPloeg2;

    @Before
    public void init(){
        //1. maak persoon aan
        testPersoon = new Persoon.PersoonBuilder()
                .adres("202 Ergens Ginderachter 1001")
                .email("persoon@test.be")
                .geboortedatum(new Date())
                .geslacht("V")
                .gsm("0491919191")
                .voornaam("test")
                .naam("persoon")
                .wachtwoord("testwachtwoord")
                .build();
        persoonRepository.save(testPersoon);
        //2. maak eerste ploeg aan
        testploeg1 = new Ploeg.PloegBuilder()
                .naam("Eerste ploeg")
                .build();
        ploegRepository.save(testploeg1);
        //3. maak tweede ploegaan
        testPloeg2 = new Ploeg.PloegBuilder()
                .naam("Tweede ploeg")
                .build();
        ploegRepository.save(testPloeg2);
        //4. maak wedstrijd aan
        testWedstrijd = new Wedstrijd.WedstrijdBuilder()
                .tijdstip(LocalDateTime.of(2015, 1, 29, 19, 30, 40))
                .locatie("Voetbal stadium Brussel")
                .tegenstander(testploeg1.getId())
                .thuisPloeg(testPloeg2.getId())
                .build();
    }

    @Test
    void maakGeldigBerichtGeeftGeenError(){
        Bericht bericht = new Bericht.BerichtBuilder()
        .boodschap("Test boodschap")
        .afzenderId(testploeg1.getId())
        .wedstrijdId(testWedstrijd.getId())
        .tijdstip(LocalDateTime.of(2015, 1, 29, 19, 30, 40))
        .build();
        berichtRepository.save(bericht);
        assertNotNull(bericht.getId());
        Optional<Bericht> found = berichtRepository.findById(bericht.getId());
        assertTrue(found.isPresent());
        assertEquals(found.get().getBoodschap(),bericht.getBoodschap());
        assertEquals(found.get().getAfzenderId(),bericht.getAfzenderId());
        assertEquals(found.get().getTijdstip(),bericht.getTijdstip());
        assertEquals(found.get().getWedstrijdId(),bericht.getWedstrijdId());
    }
}
