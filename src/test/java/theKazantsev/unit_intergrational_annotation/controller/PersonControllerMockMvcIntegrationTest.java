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
    public void givenPerson_whenAdd_thenStatus201andPersonReturned() throws Exception {

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
    public void givenId_whenGetExistingPerson_thenStatus200andPersonReturned() throws Exception {

        long id = createTestPerson("Michail").getId();

        mockMvc.perform(
                get("/persons/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value("Mockhail"));
    }


    @Test
    public void givenId_whenGetNotExistingPerson_thenStatus404anExceptionThrown() throws Exception {

        mockMvc.perform(
                get("/persons/1"))
            .andExpect(status().isNotFound())
            .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class));
    }

    @Test
    public void givePerson_whenUpdate_thenStatus200andUpdatedReturns() throws Exception {

        long id = createTestPerson("Mock").getId();

        mockMvc.perform(
                put("/persons/{id}", id)
                    .content(objectMapper.writeValueAsString(new Person("Mockhail")))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("Mockhail"));
    }

    @Test
    public void givenPerson_whenDeletePerson_thenStatus200() throws Exception {

        Person person = createTestPerson("Mock");

        mockMvc.perform(
                delete("/persons/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(person)));
    }

    @Test
    public void givenPersons_whenGetPersons_thenStatus200() throws Exception {

        Person p1 = createTestPerson("Mock");
        Person p2 =createTestPerson( "Mockhail");

        mockMvc.perform(
                get("/persons"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(p1, p2))));
    }

    private Person createTestPerson(String name) {
        Person person = new Person(name);
        return repository.save(person);
    }
}