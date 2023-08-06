package theKazantsev.unit_intergrational_annotation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import theKazantsev.unit_intergrational_annotation.dao.PersonRepository;
import theKazantsev.unit_intergrational_annotation.exception.EntityNotFoundException;
import theKazantsev.unit_intergrational_annotation.model.Person;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.sql.init.data-locations=classpath:data-tests.sql")
public class PersonControllerMockMvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void givenPersonWhenAddThenStatus201andPersonReturned() throws Exception {
        Person person = new Person("Mockhail");

        mockMvc.perform(
                post("/persons")
                    .content(objectMapper.writeValueAsString(person))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("Mockhail"));
    }

    @Test
    public void givenIdWhenGetExistingPersonThenStatus200andPersonReturned() throws Exception {
        long id = createTestPersonInDB("Michail").getId();

        mockMvc.perform(
                get("/persons/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value("Mockhail"));
    }

    @Test
    public void givenIdWhenGetNotExistingPersonThenStatus404anExceptionThrown() throws Exception {
        mockMvc.perform(
                get("/persons/1"))
            .andExpect(status().isNotFound())
            .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void givePersonWhenUpdateThenStatus200andUpdatedReturns() throws Exception {
        Person person = createTestPersonInDB("Mock");
        person.setName("Mockhail");

        mockMvc.perform(
                put("/persons/{id}", person.getId())
                    .content(objectMapper.writeValueAsString(person))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("Mockhail"));
    }

    @Test
    public void givenPersonWhenDeletePersonThenStatus200() throws Exception {
        Person person = createTestPersonInDB("Mock");

        mockMvc.perform(
                delete("/persons/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(person)));
    }

    @Test
    public void givenPersonsWhenGetPersonsThenStatus200() throws Exception {
        Person p1 = createTestPersonInDB("Mock");
        Person p2 = createTestPersonInDB( "Mockhail");

        mockMvc.perform(
                get("/persons"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(p1, p2))));
    }

    private Person createTestPersonInDB(String name) {
        Person person = new Person(name);
        return repository.save(person);
    }
}