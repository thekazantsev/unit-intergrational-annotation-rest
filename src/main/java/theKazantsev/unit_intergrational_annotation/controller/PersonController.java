package theKazantsev.unit_intergrational_annotation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import theKazantsev.unit_intergrational_annotation.dao.PersonRepository;
import theKazantsev.unit_intergrational_annotation.exception.EntityNotFoundException;
import theKazantsev.unit_intergrational_annotation.model.Person;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public ResponseEntity<List<Person>> listAllPersons() {

        List<Person> persons = personRepository.findAll();
        return ResponseEntity.ok().body(persons);
    }

    @GetMapping(value = "/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable("personId") Long personId) throws EntityNotFoundException {
        Optional<Person> person = personRepository.findById(personId);

        if (!person.isPresent())
            throw new EntityNotFoundException("id-" + personId);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(person.get());
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody @Valid Person person) {
        Person p = personRepository.save(person);

        return ResponseEntity.status(201).body(p);
    }

    @PutMapping(value = "/{personId}")
    public ResponseEntity<Person> updatePerson(@RequestBody @Valid Person person,
                                               @PathVariable("personId") Long personId) throws EntityNotFoundException {
        Optional<Person> p = personRepository.findById(personId);

        if(!p.isPresent()) throw new EntityNotFoundException("id-" + personId);

        return ResponseEntity.ok().body(personRepository.save(person));
    }



    @DeleteMapping(value = "/{personId}")
    public ResponseEntity<Person> deletePerson(@PathVariable("personId") Long personId) throws EntityNotFoundException {
        Optional<Person> p = personRepository.findById(personId);

        if(!p.isPresent()) throw new EntityNotFoundException("id-" + personId);
        personRepository.deleteById(personId);

        return ResponseEntity.ok().body(p.get());
    }
}
