package bg.codeacdemy.GRADING.dao;

import bg.codeacdemy.GRADING.bean.PersonGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GradeDAO
{
  void addGrade(String personId, String gradeA, String gradeB, String gradeC);

  void deleteGrade(String id);

  void updateGrade(String personId, String gradeA, String gradeB, String gradeC);

  PersonGrade getPGradeById(String id);

  Page<PersonGrade> search(String gradeA, String gradeB, String gradeC, Pageable pageable);

  Page<PersonGrade> findAll(Pageable pageable);


}
