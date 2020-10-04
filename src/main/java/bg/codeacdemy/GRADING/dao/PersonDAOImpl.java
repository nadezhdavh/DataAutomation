package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.Person;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonDAOImpl implements PersonDAO
{
  private final NamedParameterJdbcOperations template;

  public PersonDAOImpl(NamedParameterJdbcOperations template)
  {
    this.template = template;
  }

  @Override
  public void addPerson(String name, String surname, String egn)
  {
    String sql = "INSERT INTO person(person_id, given_name, surname, egn) " +
        "VALUES( NEXT_VAL, :given_name,:surname, :egn)";
    template.update(sql, new MapSqlParameterSource("given_name", name)
        .addValue("surname", surname)
        .addValue("egn", egn));
  }

  @Override
  public void updatePerson(String name, String surname, String egn, String id)
  {
    String sql = "UPDATE person SET given_name = COALESCE( :given_name , given_name)" +
        ", surname = COALESCE( :surname , surname), egn = COALESCE( :egn , egn)" +
        " WHERE person_id = :person_id ";
    template.update(sql, new MapSqlParameterSource("given_name", name)
        .addValue("surname", surname)
        .addValue("egn", egn)
        .addValue("person_id", id));
  }

  @Override
  public Person getPersonById(String personId)
  {
    try {
      SqlParameterSource param = new MapSqlParameterSource("person_id", personId);
      String sql = "SELECT person_id , given_name, surname, egn " +
          "FROM PERSON WHERE person_id = :person_id ";
      return template.queryForObject(sql, param, new PersonRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void deletePerson(String id)
  {
    String sql = "DELETE FROM person WHERE person_id= :person_id ";
    template.update(sql, new MapSqlParameterSource("person_id", id));
  }

  @Override
  public List<Person> searchPeople(String value)
  {
    try {
      String sql = "SELECT person_id , given_name , surname , egn  " +
          "FROM person WHERE UPPER(given_name) LIKE UPPER('" + value + "%') " +
          "OR UPPER(surname) LIKE UPPER ('" + value + "%') " +
          "OR UPPER(egn) LIKE UPPER('" + value + "%')";
      return template.query(sql, new PersonRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public Page<Person> getAllPaged(Pageable pageable)
  {

    String sort = pageable.getSort().toString().replaceAll(":", "");
    String total = "SELECT person_id, given_name, surname, egn FROM person ";
    List<Person> listTotal;
    try {
      listTotal = template.query(total, new PersonRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
    String sql = "SELECT PERSON_ID, GIVEN_NAME, SURNAME, EGN FROM person ORDER BY " + sort + "" +
        " OFFSET " + pageable.getPageNumber() * pageable.getPageSize() + " " +
        "ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY";
    List<Person> people = template.query(sql, new PersonRowMapper());
    return new PageImpl<>(people, pageable, listTotal.size());
  }

  public static class PersonRowMapper implements RowMapper<Person>
  {
    @Override
    public Person mapRow(ResultSet rs, int i) throws SQLException
    {
      return new Person(
          rs.getString("person_id"),
          rs.getString("given_name"),
          rs.getString("surname"),
          rs.getString("EGN"));
    }
  }

}
