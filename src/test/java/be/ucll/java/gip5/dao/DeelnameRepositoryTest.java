package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.AbstractIntegrationTest;
import be.ucll.java.gip5.model.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class DeelnameRepositoryTest  extends AbstractIntegrationTest {
    @Autowired
    DeelnameRepository deelnameRepository;
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
    void maakDeelname(){
        Deelname deelname = new Deelname.DeelnameBuilder()
                .id(1L)
                .commentaar("Test commentaar")
                .persoonId(testPersoon.getId())
                .wedstrijdId(testWedstrijd.getId())
                .build();
                deelnameRepository.save(deelname);
                assertNotNull(deelname.getId());
                Optional<Deelname> found = deelnameRepository.findById(deelname.getId());
                assertTrue(found.isPresent());
                assertEquals(found.get().getId(), deelname.getId());
                assertEquals(found.get().getCommentaar(), deelname.getCommentaar());
                assertEquals(found.get().getPersoonId(), deelname.getPersoonId());
                assertEquals(found.get().getWedstrijdId(), deelname.getWedstrijdId());
    }
}
