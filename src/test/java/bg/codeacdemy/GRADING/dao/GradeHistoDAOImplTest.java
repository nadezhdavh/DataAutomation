package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.PersonOldGrade;
import oracle.jdbc.OracleDriver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;

public class GradeHistoDAOImplTest
{
  GradeHistoDAOImpl gradeHistoDAOImpl;

  @SuppressWarnings("unused")
  @BeforeMethod
  public void setUp()
  {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setUrl("jdbc:oracle:thin:@//83.228.124.173:6223/nadezhda_hristeva");
    dataSource.setUsername("nadezhda_hristeva");
    dataSource.setPassword("dbpass");
    dataSource.setDriverClass(OracleDriver.class);
    gradeHistoDAOImpl = new GradeHistoDAOImpl(new NamedParameterJdbcTemplate(dataSource));

  }

  @Test
  public void testGetOldGradesPaged()
  {
    Page<PersonOldGrade> list = gradeHistoDAOImpl.getHistory(PageRequest.of(0, 1, Sort.by("histo_dt")));
    assertFalse(list.isEmpty());
  }

  @Test
  public void testGetOldGradesyEGN()
  {
    List<PersonOldGrade> list = gradeHistoDAOImpl.search("ID00000062", "2020-07-20");
    assertFalse(list.isEmpty());
  }

}