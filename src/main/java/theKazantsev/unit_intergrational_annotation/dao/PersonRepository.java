package theKazantsev.unit_intergrational_annotation.dao;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import theKazantsev.unit_intergrational_annotation.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
