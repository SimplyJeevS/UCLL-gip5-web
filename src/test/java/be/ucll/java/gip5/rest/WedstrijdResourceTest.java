package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.*;
import be.ucll.java.gip5.dto.BerichtDTO;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Wedstrijd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WedstrijdResourceTest {

    @Autowired
    WedstrijdResource wedstrijdResource;
    @Autowired
    WedstrijdRepository wedstrijdRepository;

    Wedstrijd testwedstrijd;
    Ploeg testthuisploeg;
    Ploeg testtegenstander;

    @RunWith(MockitoJUnitRunner.class)
    public class TestWedStrijd{

        @Mock
        WedstrijdResource mockwedstrijdResource;
        @InjectMocks
        @Autowired
        WedstrijdRepository wedstrijdRepository;
        @Autowired
        PloegRepository ploegRepository;
        @Autowired
        ToewijzingRepository toewijzingRepository;
        @Autowired
        DeelnameRepository deelnameRepository;
        WedstrijdResource resourceController = new WedstrijdResource(wedstrijdRepository, ploegRepository,  toewijzingRepository, deelnameRepository);

        @Before
        public void init(){
            testthuisploeg = new Ploeg.PloegBuilder()
                    .naam("thuisploeg")
                    .build();
            ploegRepository.save(testthuisploeg);
            testtegenstander = new Ploeg.PloegBuilder()
                    .naam("tegenstander")
                    .build();
            ploegRepository.save(testtegenstander);
            testwedstrijd = new Wedstrijd.WedstrijdBuilder()
                    .id(1L)
                    .locatie("Haasrode")
                    .tegenstander(1L)
                    .thuisPloeg(2L)
                    .build();
        }

        @Test
        public void testPostWedstrijd() throws NotFoundException, ParameterInvalidException {     //Testen of het posten van een wedstrijd werkt

            WedstrijdDTO wedstrijd = new WedstrijdDTO();

            resourceController.postWedstrijd(wedstrijd);

            verify(wedstrijdResource, times(1)).postWedstrijd(wedstrijd);
        }

        @Test
        public void testGetWedstrijd() throws NotFoundException, ParameterInvalidException {     //Testen of het getten van een wedstrijd werkt

            WedstrijdDTO wedstrijd = new WedstrijdDTO();

            resourceController.getWedstrijd(1L);

            verify(wedstrijdResource, times(1)).getWedstrijd(1L);

            Assert.assertEquals(wedstrijd.getLocatie(), testwedstrijd.getLocatie());
        }
    }
}
