package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.PersonGrade;
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
public class GradeDAOImpl implements GradeDAO
{
  private final NamedParameterJdbcOperations template;

  public GradeDAOImpl(NamedParameterJdbcOperations template)
  {
    this.template = template;
  }

  @Override
  public void addGrade(String personId, String gradeA, String gradeB, String gradeC)
  {
    String sql = "INSERT INTO grade(person_id, grade_a, grade_b, grade_c) " +
        "VALUES(:person_id, :grade_a, :grade_b, :grade_c)";
    template.update(sql, new MapSqlParameterSource("person_id", personId)
        .addValue("grade_a", gradeA)
        .addValue("grade_b", gradeB)
        .addValue("grade_c", gradeC));
  }

  @Override
  public void updateGrade(String personId, String gradeA, String gradeB, String gradeC)
  {
    String sql = "UPDATE grade SET grade_a = COALESCE( :grade_a, grade_a), " +
        "grade_b =  COALESCE( :grade_b , grade_b), " +
        "grade_c = COALESCE( :grade_c , grade_c) WHERE person_id = :person_id ";
    template.update(sql, new MapSqlParameterSource(
        "grade_a", gradeA)
        .addValue("grade_b", gradeB)
        .addValue("grade_c", gradeC)
        .addValue("person_id", personId)
    );
  }

  @Override
  public PersonGrade getPGradeById(String id)
  {
    try {
      MapSqlParameterSource param = new MapSqlParameterSource("person_id", id);
      String sql = "SELECT g.grade_a, g.grade_b, g.grade_c, p.given_name, p.surname, p.egn FROM grade g INNER JOIN person p ON g.person_id = p.person_id WHERE g.person_id = :person_id ";
      return template.queryForObject(sql, param, new PersonGradeRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void deleteGrade(String id)
  {
    String sql = "DELETE FROM grade WHERE person_id= :person_id ";
    template.update(sql, new MapSqlParameterSource("person_id", id));
  }

  @Override
  public Page<PersonGrade> search(String gradeA, String gradeB, String gradeC, Pageable pageable)
  {

    String sort = pageable.getSort().toString().replaceAll(":", "");
    List<PersonGrade> listTotal;
    SqlParameterSource param = new MapSqlParameterSource("grade_a", gradeA)
        .addValue("grade_b", gradeB)
        .addValue("grade_c", gradeC);
    String total = "SELECT g.person_id, g.grade_a, g.grade_b, g.grade_c," +
        " p.given_name, p.surname, p.egn " +
        "FROM grade g INNER JOIN person p ON g.person_id = p.person_id " +
        "WHERE grade_a=COALESCE( :grade_a , grade_a) " +
        "AND grade_b =  COALESCE( :grade_b, grade_b) " +
        "AND grade_c = COALESCE( :grade_c, grade_c) ";
    try {
      listTotal = template.query(total, param, new PersonGradeRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
    String sql = "SELECT g.person_id, g.grade_a, g.grade_b, g.grade_c, " +
        "p.given_name, p.surname, p.egn FROM grade g " +
        "INNER JOIN person p ON g.person_id = p.person_id " +
        "WHERE grade_a=  COALESCE( :grade_a , grade_a) " +
        "AND grade_b =  COALESCE( :grade_b, grade_b) " +
        "AND grade_c = COALESCE( :grade_c, grade_c) " +
        "ORDER BY " + sort + " OFFSET " + pageable.getPageNumber() * pageable.getPageSize()
        + "  ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY ";
    List<PersonGrade> list = template.query(sql, param, new PersonGradeRowMapper());
    return new PageImpl<>(list, pageable, listTotal.size());

  }

  @Override
  public Page<PersonGrade> findAll(Pageable pageable)
  {

    String sort = pageable.getSort().toString().replaceAll(":", "");
    List<PersonGrade> listTotal;

    String total = "SELECT g.person_id, g.grade_a, g.grade_b, " +
        "g.grade_c, p.given_name, p.surname, p.egn " +
        "FROM grade g INNER JOIN person p ON g.person_id = p.person_id ";
    try {
      listTotal = template.query(total, new PersonGradeRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
    String sql = "SELECT g.person_id, g.grade_a, g.grade_b, g.grade_c, p.given_name, " +
        "p.surname, p.egn FROM grade g " +
        "INNER JOIN person p ON g.person_id = p.person_id  " +
        "ORDER BY " + sort + " " +
        "OFFSET " + pageable.getPageNumber() * pageable.getPageSize() +
        " ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY ";
    List<PersonGrade> list = template.query(sql, new PersonGradeRowMapper());

    return new PageImpl<>(list, pageable, listTotal.size());

  }

  public static class PersonGradeRowMapper implements RowMapper<PersonGrade>
  {
    @Override
    public PersonGrade mapRow(ResultSet rs, int i) throws SQLException
    {
      return new PersonGrade(
          rs.getString("given_name"),
          rs.getString("surname"),
          rs.getString("egn"),
          rs.getString("grade_a"),
          rs.getString("grade_b"),
          rs.getString("grade_c"));
    }
  }
}
