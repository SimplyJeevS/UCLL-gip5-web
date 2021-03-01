//package be.ucll.java.gip5.dao;
//
//import be.ucll.java.gip5.model.*;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//
//public class ToewijzingRepositoryTest {
//    @Autowired
//    ToewijzingRepository toewijzingRepository;
//    @Autowired
//    PersoonRepository persoonRepository;
//    @Autowired
//    PloegRepository ploegRepository;
//    @Autowired
//    RolRepository rolRepository;
//
//    private Persoon testPersoon;
//    private Ploeg testploeg1,testPloeg2;
//    private Rol testrol;
//
//    @Before
//    public void init(){
//        //1. maak persoon aan
//        testPersoon = new Persoon.PersoonBuilder()
//                .adres("202 Ergens Ginderachter 1001")
//                .email("persoon@test.be")
//                .geboortedatum(new Date())
//                .geslacht("V")
//                .gsm("0491919191")
//                .voornaam("test")
//                .naam("persoon")
//                .wachtwoord("testwachtwoord")
//                .build();
//        persoonRepository.save(testPersoon);
//        //2. maak eerste ploeg aan
//        testploeg1 = new Ploeg.PloegBuilder()
//                .naam("Eerste ploeg")
//                .build();
//        ploegRepository.save(testploeg1);
//        //3. maak tweede ploegaan
//        testPloeg2 = new Ploeg.PloegBuilder()
//                .naam("Tweede ploeg")
//                .build();
//        ploegRepository.save(testPloeg2);
//        //4. Maak rol aan
//        testrol = new Rol.RolBuilder()
//                .id(1L)
//                .naam("testRol")
//                .build();
//    }
//    @Test
//    @Transactional
//    @Rollback(true)
//    public void MaakToewijzing(){
//        Toewijzing testToewijzing = new Toewijzing.ToewijzingBuilder()
//                .id(1L)
//                .persoonId(testPersoon.getId())
//                .ploegId(testploeg1.getId())
//                .rolId(testrol.getId())
//                .build();
//        toewijzingRepository.save(testToewijzing);
//
//        List<Toewijzing> toewijzingen = toewijzingRepository.findAll();
//        Assert.assertEquals(1, toewijzingen.size());
//
//        Assert.assertEquals(testToewijzing.getId(), toewijzingen.get(0).getId());
//        Assert.assertEquals(testToewijzing.getPersoonId(), toewijzingen.get(0).getPersoonId());
//        Assert.assertEquals(testToewijzing.getPloegId(), toewijzingen.get(0).getPloegId());         //of de test data wel degelijk overeen komt
//        Assert.assertEquals(testToewijzing.getRolId(), toewijzingen.get(0).getRolId());            // met die van de repo
//                                                                                                  // test data zelf is hardcoded dus zou voldoende moeten zijn
//    }
//}
