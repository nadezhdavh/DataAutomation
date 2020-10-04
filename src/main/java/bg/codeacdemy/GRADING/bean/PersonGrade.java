package bg.codeacdemy.GRADING.bean;

public class PersonGrade
{
  private String givenName;
  private String surname;
  private String egn;
  private String gradeA;
  private String gradeB;
  private String gradeC;

  public PersonGrade()
  {
  }

  public PersonGrade(String givenName, String surname, String egn, String gradeA, String gradeB, String gradeC)
  {
    this.givenName = givenName;
    this.surname = surname;
    this.egn = egn;
    this.gradeA = gradeA;
    this.gradeB = gradeB;
    this.gradeC = gradeC;
  }

  public String getGivenName()
  {
    return givenName;
  }

  public void setGivenName(String givenName)
  {
    this.givenName = givenName;
  }

  public String getSurname()
  {
    return surname;
  }

  public void setSurname(String surname)
  {
    this.surname = surname;
  }

  public String getEgn()
  {
    return egn;
  }

  public void setEgn(String egn)
  {
    this.egn = egn;
  }

  public String getGradeA()
  {
    return gradeA;
  }

  public void setGradeA(String gradeA)
  {
    this.gradeA = gradeA;
  }

  public String getGradeB()
  {
    return gradeB;
  }

  public void setGradeB(String gradeB)
  {
    this.gradeB = gradeB;
  }

  public String getGradeC()
  {
    return gradeC;
  }

  public void setGradeC(String gradeC)
  {
    this.gradeC = gradeC;
  }
}
