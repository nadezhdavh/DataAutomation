package bg.codeacdemy.GRADING.Service;

import bg.codeacdemy.GRADING.bean.PersonOldGrade;
import bg.codeacdemy.GRADING.dao.GradeHistoDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeHistoServiceImpl implements GradeHistoService
{
  final
  GradeHistoDAO gradeHistoDAO;

  public GradeHistoServiceImpl(GradeHistoDAO gradeHistoDAO)
  {
    this.gradeHistoDAO = gradeHistoDAO;
  }

  @Override
  public Page<PersonOldGrade> getHistory(Pageable pageable)
  {
    return gradeHistoDAO.getHistory(pageable);
  }

  @Override
  public List<PersonOldGrade> search(String value, String date)
  {
    return gradeHistoDAO.search(value, date);
  }
}
