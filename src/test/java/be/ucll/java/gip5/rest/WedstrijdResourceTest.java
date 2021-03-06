package be.ucll.java.gip5.rest;

import be.ucll.java.gip5.AbstractIntegrationTest;
import be.ucll.java.gip5.dao.*;
import be.ucll.java.gip5.dto.BerichtDTO;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Wedstrijd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WedstrijdResourceTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    WedstrijdResource wedstrijdResource;
    @Autowired
    WedstrijdRepository wedstrijdRepository;



//        @Autowired
//        PloegRepository ploegRepository;
//        @Autowired
//        ToewijzingRepository toewijzingRepository;
//        @Autowired
//        DeelnameRepository deelnameRepository;
//        WedstrijdResource resourceController = new WedstrijdResource(wedstrijdRepository, ploegRepository,  toewijzingRepository, deelnameRepository);
//
//        @Autowired
//        private WebApplicationContext wac;
//        @BeforeEach
//        void setUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//
//        @Test
//        public void testPostWedstrijd() throws Exception {     //Testen of het posten van een wedstrijd werkt
//            Ploeg testthuisploeg = new Ploeg.PloegBuilder()
//                    .naam("thuisploeg")
//                    .build();
//            Ploeg testtegenstander = new Ploeg.PloegBuilder()
//                    .naam("tegenstander")
//                    .build();
//            Wedstrijd testwedstrijd = new Wedstrijd.WedstrijdBuilder()
//                    .locatie("Haasrode")
//                    .tegenstander(testtegenstander.getId())
//                    .thuisPloeg(testthuisploeg.getId())
//                    .build();
//
//
//            MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/wedstrijd")
//                    .content(toJson(testwedstrijd))
//                    .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").exists())
//                    .andReturn();
//            Wedstrijd gemaakteWedstrijd = fromMvcResult(mvcResult, Wedstrijd.class);
//
//            assertEquals(gemaakteWedstrijd.getLocatie(), testwedstrijd.getLocatie());
//        }
//
//        @Test
//        public void testGetWedstrijdList() throws Exception {     //Testen of het getten van een wedstrijd werkt
//            mockMvc.perform(
//                    MockMvcRequestBuilders.get("/wedstrijd")
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andReturn();
//        }
//    @Test
//    public void testDeleteWedstrijd() throws Exception {     //Testen of het posten van een wedstrijd werkt
//        Ploeg testthuisploeg = new Ploeg.PloegBuilder()
//                .naam("thuisploeg")
//                .build();
//        Ploeg testtegenstander = new Ploeg.PloegBuilder()
//                .naam("tegenstander")
//                .build();
//        Wedstrijd testwedstrijd = new Wedstrijd.WedstrijdBuilder()
//                .locatie("Haasrode")
//                .tegenstander(testtegenstander.getId())
//                .thuisPloeg(testthuisploeg.getId())
//                .build();
//        wedstrijdRepository.save(testwedstrijd);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/wedstrijd/" + testwedstrijd.getId().toString())
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//    @Test
//    public void testEditWedstrijd() throws Exception {     //Testen of het posten van een wedstrijd werkt
//        Ploeg testthuisploeg = new Ploeg.PloegBuilder()
//                .naam("thuisploeg")
//                .build();
//        Ploeg testtegenstander = new Ploeg.PloegBuilder()
//                .naam("tegenstander")
//                .build();
//        Wedstrijd testwedstrijd = new Wedstrijd.WedstrijdBuilder()
//                .locatie("Haasrode")
//                .tegenstander(testtegenstander.getId())
//                .thuisPloeg(testthuisploeg.getId())
//                .build();
//        wedstrijdRepository.save(testwedstrijd);
//        Wedstrijd updatedWedstrijd = new Wedstrijd.WedstrijdBuilder()
//                .locatie("Leuven")
//                .tegenstander(testtegenstander.getId())
//                .thuisPloeg(testthuisploeg.getId())
//                .build();
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/wedstrijd/" + testwedstrijd.getId().toString())
//                .content(toJson(updatedWedstrijd))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//    }

}
