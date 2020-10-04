package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.PersonOldGrade;
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
public class GradeHistoDAOImpl implements GradeHistoDAO
{
  private final NamedParameterJdbcOperations template;

  public GradeHistoDAOImpl(NamedParameterJdbcOperations template)
  {
    this.template = template;
  }

  @Override
  public Page<PersonOldGrade> getHistory(Pageable pageable)
  {
    try {
      String sort = pageable.getSort().toString().replaceAll(":", "");

      String total = "SELECT g.person_id, g.grade_a, g.grade_b, g.grade_c, g.histo_dt," +
          " g.op, p.given_name, p.surname, p.egn " +
          "FROM grade_histo g INNER JOIN person p ON g.person_id = p.person_id ";
      List<PersonOldGrade> totalElements = template.query(total, new GradeHistoRowMapper());

      String sql = "SELECT g.person_id, g.grade_a, g.grade_b, g.grade_c, g.histo_dt, g.op, p.given_name," +
          " p.surname, p.egn " +
          "FROM grade_histo g INNER JOIN person p ON g.person_id = p.person_id " +
          "ORDER BY " + sort + " OFFSET " + pageable.getPageSize() * pageable.getPageNumber() + " " +
          "ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY ";
      List<PersonOldGrade> list = template.query(sql, new GradeHistoRowMapper());

      return new PageImpl<>(list, pageable, totalElements.size());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public List<PersonOldGrade> search(String value, String date)
  {
    try {
      SqlParameterSource param = new MapSqlParameterSource("value", value)
          .addValue("date", date);
      String sql = "SELECT g.person_id, g.grade_a, g.grade_b, g.grade_c, g.histo_dt, " +
          "g.op, p.given_name, p.surname, p.egn " +
          "FROM grade_histo g INNER JOIN person p ON g.person_id = p.person_id " +
          "WHERE p.egn = :value OR p.person_id = :value " +
          "AND g.histo_dt = NVL(CAST( :date AS DATE),g.histo_dt)" +
          " ORDER BY g.histo_dt DESC ";
      return template.query(sql, param, new GradeHistoRowMapper());
    }
    catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public static class GradeHistoRowMapper implements RowMapper<PersonOldGrade>
  {
    @Override
    public PersonOldGrade mapRow(ResultSet rs, int i) throws SQLException
    {
      return new PersonOldGrade(
          rs.getString("given_name"),
          rs.getString("surname"),
          rs.getString("egn"),
          rs.getString("grade_a"),
          rs.getString("grade_b"),
          rs.getString("grade_c"),
          rs.getDate("histo_dt"),
          rs.getString("op"));
    }
  }
}