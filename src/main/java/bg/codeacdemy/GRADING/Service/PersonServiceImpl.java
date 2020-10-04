package bg.codeacdemy.GRADING.Service;

import bg.codeacdemy.GRADING.bean.Person;
import bg.codeacdemy.GRADING.dao.PersonDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService
{
  final
  PersonDAO personDAO;

  public PersonServiceImpl(PersonDAO personDAO)
  {
    this.personDAO = personDAO;
  }

  @Override
  public void addPerson(String name, String surname, String egn)
  {
    personDAO.addPerson(name, surname, egn);
  }

  @Override
  public void updatePerson(String name, String surname, String egn, String id)
  {
    personDAO.updatePerson(name, surname, egn, id);
  }

  @Override
  public void deletePerson(String id)
  {
    personDAO.deletePerson(id);
  }


  @Override
  public Person getPersonById(String personId)
  {
    return personDAO.getPersonById(personId);
  }

  @Override
  public List<Person> searchPeople(String value)
  {
    return personDAO.searchPeople(value);
  }

  @Override
  public Page<Person> getAllPaged(Pageable pageable)
  {
    return personDAO.getAllPaged(pageable);
  }
}
