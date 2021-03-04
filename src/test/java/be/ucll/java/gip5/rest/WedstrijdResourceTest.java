package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Wedstrijd;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

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
        BerichtResource resourceController = new WedstrijdResource();

        @Before
        public void init(){
            testthuisploeg = new Ploeg.PloegBuilder()
                    .id(2L)
                    .naam("thuisploeg")
                    .build();
            testtegenstander = new Ploeg.PloegBuilder()
                    .id(1L)
                    .naam("tegenstander")
                    .build();
            testwedstrijd = new Wedstrijd.WedstrijdBuilder()
                    .id(1L)
                    .locatie("Haasrode")
                    .tegenstander(1L)
                    .thuisPloeg(2L)
                    .build();
        }
    }
}
