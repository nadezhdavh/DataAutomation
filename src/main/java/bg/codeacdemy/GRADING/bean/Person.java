package bg.codeacdemy.GRADING.bean;

public class Person
{
  private String personId;
  private String givenName;
  private String surname;
  private String EGN;

  public Person()
  {

  }

  public Person(String person_id, String given_name, String surname, String egn)
  {
    this.personId = person_id;
    this.givenName = given_name;
    this.surname = surname;
    this.EGN = egn;
  }

  public String getPersonId()
  {
    return personId;
  }

  public void setPersonId(String personId)
  {
    this.personId = personId;
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

  public String getEGN()
  {
    return EGN;
  }

  public void setEGN(String EGN)
  {
    this.EGN = EGN;
  }
}

