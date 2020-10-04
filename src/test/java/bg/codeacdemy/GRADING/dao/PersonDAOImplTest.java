package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.Person;
import oracle.jdbc.OracleDriver;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;

public class PersonDAOImplTest
{
  PersonDAOImpl personDAOImpl;

  @SuppressWarnings("unused")
  @BeforeMethod
  public void setUp()
  {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setUrl("jdbc:oracle:thin:@//83.228.124.173:6223/nadezhda_hristeva");
    dataSource.setUsername("nadezhda_hristeva");
    dataSource.setPassword("dbpass");
    dataSource.setDriverClass(OracleDriver.class);
    personDAOImpl = new PersonDAOImpl(new NamedParameterJdbcTemplate(dataSource));
  }

  @Test
  public void testGetPeoplePaged()
  {
    Page<Person> list = personDAOImpl.getAllPaged(PageRequest.of(1, 4, Sort.by("given_name")));
    System.out.println(list);
    assertFalse(list.isEmpty());
  }

  @Test
  public void testSearchPeople()
  {
    List<Person> list = personDAOImpl.searchPeople("Iva");
    assertFalse(list.isEmpty());
  }

  @Test
  public void testAddPerson()
  {
    personDAOImpl.addPerson("Agel", "Todorov", RandomStringUtils.randomNumeric(10));
  }

  @Test
  public void testUpdatePerson()
  {
    personDAOImpl.updatePerson("", "", RandomStringUtils.randomNumeric(10), "ID00000029");
  }


  @Test
  public void testDeletePerson()
  {
    personDAOImpl.deletePerson("ID00000059");
  }


}