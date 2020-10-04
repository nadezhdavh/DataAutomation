package bg.codeacdemy.GRADING.Service;

import bg.codeacdemy.GRADING.bean.PersonGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GradeService
{
  void addGrade(String personId, String gradeA, String gradeB, String gradeC);

  void deleteGrade(String id);

  void updateGrade(String personId, String gradeA, String gradeB, String gradeC);

  Page<PersonGrade> search(String gradeA, String gradeB, String gradeC, Pageable pageable);

  PersonGrade getPGradeById(String id);

  Page<PersonGrade> findAll(Pageable pageable);
}
