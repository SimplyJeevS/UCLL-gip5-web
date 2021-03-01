package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Persoon;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public class PersoonRepositoryTest {
    @Autowired
    PersoonRepository persoonRepository;

    @Test
    @Transactional
    @Rollback(true)
    public void MaakPersoon(){
        Persoon testPersoon = new Persoon.PersoonBuilder()
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

        List<Persoon> personen = persoonRepository.findAll();
        Assert.assertEquals(1, personen.size());

        Assert.assertEquals(testPersoon.getAdres(), personen.get(0).getAdres());
        Assert.assertEquals(testPersoon.getEmail(), personen.get(0).getEmail());
        Assert.assertEquals(testPersoon.getGeboortedatum(), personen.get(0).getGeboortedatum());        //of de test data wel degelijk overeen komt
        Assert.assertEquals(testPersoon.getGeslacht(), personen.get(0).getGeslacht());                  // met die van de repo
        Assert.assertEquals(testPersoon.getGsm(), personen.get(0).getGsm());                            // test data zelf is hardcoded dus zou voldoende moeten zijn
        Assert.assertEquals(testPersoon.getVoornaam(), personen.get(0).getVoornaam());
        Assert.assertEquals(testPersoon.getNaam(), personen.get(0).getNaam());
        Assert.assertEquals(testPersoon.getWachtwoord(), personen.get(0).getWachtwoord());
    }
}
