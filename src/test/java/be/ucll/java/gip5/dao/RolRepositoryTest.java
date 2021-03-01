//package be.ucll.java.gip5.dao;
//
//import be.ucll.java.gip5.model.Persoon;
//import be.ucll.java.gip5.model.Rol;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//import java.util.Date;
//import java.util.List;
//
//public class RolRepositoryTest {
//    @Autowired
//    RolRepository rolRepository;
//
//    @Test
//    @Transactional
//    @Rollback(true)
//    public void MaakRol() {
//        Rol testRol = new Rol.RolBuilder()
//                .id(1L)
//                .naam("TestRol")
//                .build();
//        rolRepository.save(testRol);
//
//        List<Rol> rollen = rolRepository.findAll();
//        Assert.assertEquals(1, rollen.size());
//
//        Assert.assertEquals(testRol.getId(), rollen.get(0).getId());     //of de test data wel degelijk overeen komt
//        Assert.assertEquals(testRol.getNaam(), rollen.get(0).getNaam()); // met die van de repo
//                                                                        // test data zelf is hardcoded dus zou voldoende moeten zijn
//    }
//
//
//
//}
