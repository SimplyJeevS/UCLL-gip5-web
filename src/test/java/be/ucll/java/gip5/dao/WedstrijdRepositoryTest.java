package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Deelname;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Wedstrijd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WedstrijdRepositoryTest {
    @Autowired
    WedstrijdRepository wedstrijdRepository;
    @Autowired
    PersoonRepository persoonRepository;
    @Autowired
    PloegRepository ploegRepository;

    private Ploeg testploeg1,testPloeg2;

    @Before
    public void init(){
        //1. maak eerste ploeg aan
        testploeg1 = new Ploeg.PloegBuilder()
                .naam("Eerste ploeg")
                .build();
        ploegRepository.save(testploeg1);
        //2. maak tweede ploegaan
        testPloeg2 = new Ploeg.PloegBuilder()
                .naam("Tweede ploeg")
                .build();
        ploegRepository.save(testPloeg2);
    }

    @Test
    void maakWedstrijd(){
       Wedstrijd testwedstrijd = new Wedstrijd.WedstrijdBuilder()
                .tijdstip(LocalDateTime.of(2015, 1, 29, 19, 30, 40))
                .locatie("Voetbal stadium Brussel")
                .tegenstander(testploeg1.getId())
                .thuisPloeg(testPloeg2.getId())
                .build();
        wedstrijdRepository.save(testwedstrijd);

        assertNotNull(testwedstrijd.getId());
        List<Wedstrijd> wedstrijden = wedstrijdRepository.findAll();
        Assert.assertEquals(1, wedstrijden.size());

        Assert.assertEquals(testwedstrijd.getId(), wedstrijden.get(0).getId());
        Assert.assertEquals(testwedstrijd.getLocatie(), wedstrijden.get(0).getLocatie());
        Assert.assertEquals(testwedstrijd.getTegenstander(), wedstrijden.get(0).getTegenstander());         //of de test data wel degelijk overeen komt
        Assert.assertEquals(testwedstrijd.getThuisPloeg(), wedstrijden.get(0).getThuisPloeg());            // met die van de repo
                                                                                                          // test data zelf is hardcoded dus zou voldoende moeten zijn
    }
}
