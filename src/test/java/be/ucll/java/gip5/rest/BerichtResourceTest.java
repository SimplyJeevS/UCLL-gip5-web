package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.dao.BerichtRepository;
import be.ucll.java.gip5.dto.BerichtDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Bericht;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BerichtResourceTest {
    @Autowired
    BerichtResource berichtResource;
    @Autowired
    BerichtRepository berichtRepository;

    Bericht testbericht;

    @RunWith(MockitoJUnitRunner.class)
    public class TestBericht {

        @Mock
        BerichtResource mockberichtResource;
        @InjectMocks
        BerichtResource resourceController = new BerichtResource();

        @Before
        public void init(){
            testbericht = new Bericht.BerichtBuilder()
                    .tijdstip(LocalDateTime.of(2020,12,9,9,9,9))
                    .id(1L)
                    .boodschap("test boodschap")
                    .afzenderId(2L)
                    .wedstrijdId(3L)
                    .build();
        }

        @Test
        public void testPostBericht() throws NotFoundException, ParameterInvalidException {     //Testen of het posten van een bericht werkt

            BerichtDTO bericht = new BerichtDTO();

            resourceController.postBericht(bericht);

            verify(berichtResource, times(1)).postBericht(bericht);
        }

        @Test
        public void testGetBericht() throws NotFoundException, ParameterInvalidException {     //Testen of het getten van een bericht werkt

            BerichtDTO bericht = new BerichtDTO();

            resourceController.getBericht(1L);

            verify(berichtResource, times(1)).getBericht(1L);

            Assert.assertEquals(bericht.getAfzenderId(), testbericht.getAfzenderId());
        }
    }
}
