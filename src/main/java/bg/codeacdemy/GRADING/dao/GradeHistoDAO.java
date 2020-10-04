package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.PersonOldGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GradeHistoDAO
{
  Page<PersonOldGrade> getHistory(Pageable pageable);

  List<PersonOldGrade> search(String value, String date);
}
