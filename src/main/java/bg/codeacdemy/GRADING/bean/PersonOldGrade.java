package bg.codeacdemy.GRADING.bean;

import java.util.Date;

public class PersonOldGrade extends PersonGrade
{
  private Date   date;
  private String option;

  public PersonOldGrade()
  {
  }

  public PersonOldGrade(String givenName, String surname, String egn, String gradeA, String gradeB, String gradeC, Date date, String option)
  {
    super(givenName, surname, egn, gradeA, gradeB, gradeC);
    this.date = date;
    this.option = option;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public String getOption()
  {
    return option;
  }

  public void setOption(String option)
  {
    this.option = option;
  }
}
