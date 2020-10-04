package bg.codeacdemy.GRADING.Service;


import bg.codeacdemy.GRADING.bean.PersonGrade;
import bg.codeacdemy.GRADING.dao.GradeDAO;
import bg.codeacdemy.GRADING.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl implements GradeService
{
  @Autowired
  GradeDAO  gradeDao;
  @Autowired
  PersonDAO personDAO;

  public GradeServiceImpl(GradeDAO gradeDao, PersonDAO personDAO)
  {
    this.gradeDao = gradeDao;
    this.personDAO = personDAO;
  }

  @Override
  public void addGrade(String personId, String gradeA, String gradeB, String gradeC)
  {
    gradeDao.addGrade(personId, gradeA, gradeB, gradeC);
  }

  @Override
  public void deleteGrade(String id)
  {
    gradeDao.deleteGrade(id);
  }

  @Override
  public void updateGrade(String personId, String gradeA, String gradeB, String gradeC)
  {
    gradeDao.updateGrade(personId, gradeA, gradeB, gradeC);
  }

  @Override
  public Page<PersonGrade> search(String gradeA, String gradeB, String gradeC, Pageable pageable)
  {
    return gradeDao.search(gradeA, gradeB, gradeC, pageable);
  }

  @Override
  public PersonGrade getPGradeById(String id)
  {
    return gradeDao.getPGradeById(id);
  }

  @Override
  public Page<PersonGrade> findAll(Pageable pageable)
  {
    return gradeDao.findAll(pageable);
  }
}

