package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.PersonGrade;
import oracle.jdbc.OracleDriver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

public class GradeDAOImplTest
{
  GradeDAO gradeDAO;

  @SuppressWarnings("unused")
  @BeforeMethod
  public void setUp()
  {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setUrl("jdbc:oracle:thin:@//83.228.124.173:6223/nadezhda_hristeva");
    dataSource.setUsername("nadezhda_hristeva");
    dataSource.setPassword("dbpass");
    dataSource.setDriverClass(OracleDriver.class);
    gradeDAO = new GradeDAOImpl(new NamedParameterJdbcTemplate(dataSource));

  }

  @Test
  public void testAddGrade()
  {
    gradeDAO.addGrade("FMQOK8XWOI", "B", "B", "C");
  }

  @Test
  public void testUpdateGrade()
  {
    gradeDAO.updateGrade("FMQOK8XWOI", "A", "", "");
  }

  @Test
  public void testDeleteGrade()
  {
    gradeDAO.deleteGrade("JWVG2BKNUJ");
  }

  @Test
  public void testSearch()
  {
    Page<PersonGrade> list = gradeDAO.search("A", "B", "C", PageRequest.of(0, 3, Sort.by("grade_a")));
    assertFalse(list.isEmpty());
  }

  @Test
  public void testFindAll()
  {
    Page<PersonGrade> list = gradeDAO.findAll(PageRequest.of(0, 2, Sort.by("egn")));
    assertFalse(list.isEmpty());
  }
}
