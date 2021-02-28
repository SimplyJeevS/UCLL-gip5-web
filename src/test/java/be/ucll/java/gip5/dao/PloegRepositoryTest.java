package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public class PloegRepositoryTest {

    @Autowired
    PloegRepository ploegRepository;

    @Test
    @Transactional
    @Rollback(true)
    public void MaakPloeg(){
        Ploeg testPloeg = new Ploeg.PloegBuilder()
                .id(1L)
                .naam("testNaam")
                .build();
        ploegRepository.save(testPloeg);

        List<Ploeg> ploegen = ploegRepository.findAll();
        Assert.assertEquals(1, ploegen.size());

        Assert.assertEquals(testPloeg.getId(), ploegen.get(0).getId());      //of de test data wel degelijk overeen komt
        Assert.assertEquals(testPloeg.getNaam(), ploegen.get(0).getNaam());  // met die van de repo
                                                                             // test data zelf is hardcoded dus zou voldoende moeten zijn
    }
}
