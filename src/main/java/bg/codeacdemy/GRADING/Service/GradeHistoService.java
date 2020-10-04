package bg.codeacdemy.GRADING.Service;

import bg.codeacdemy.GRADING.bean.PersonOldGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GradeHistoService
{
  Page<PersonOldGrade> getHistory(Pageable pageable);

  List<PersonOldGrade> search(String value, String date);
}
