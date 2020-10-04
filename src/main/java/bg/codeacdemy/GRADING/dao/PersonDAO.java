package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonDAO
{
  void addPerson(String name, String surname, String egn);

  void updatePerson(String name, String surname, String egn, String id);

  void deletePerson(String id);

  Person getPersonById(String id);

  List<Person> searchPeople(String value);

  Page<Person> getAllPaged(Pageable pageable);
}
