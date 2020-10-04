package bg.codeacdemy.GRADING.controller;

import bg.codeacdemy.GRADING.Service.GradeService;
import bg.codeacdemy.GRADING.Service.PersonService;
import bg.codeacdemy.GRADING.bean.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/people")
public class PersonController
{
  final
  PersonService personService;
  final
  GradeService  gradeService;

  public PersonController(PersonService personService, GradeService gradeService)
  {
    this.personService = personService;
    this.gradeService = gradeService;
  }

  @PostMapping()
  public ResponseEntity<?> addPerson(
      @RequestParam @Pattern(regexp = "^[a-zA-Z-]{2,20}$") String givenName,
      @RequestParam @Pattern(regexp = "^[a-zA-Z-]{2,20}$") String surname,
      @RequestParam @Pattern(regexp = "^[0-9]{10}$") String egn
  )
  {
    personService.addPerson(givenName, surname, egn);
    return ResponseEntity.status(201).build();
  }

  @PatchMapping(value = "/{personId}")
  public ResponseEntity<Void> updatePerson(@PathVariable String personId,
                                           @RequestParam(required = false) String givenName,
                                           @RequestParam(required = false) String surname,
                                           @RequestParam(required = false) String egn)
  {
    if ((givenName == null || givenName.matches("^[a-zA-Z-]{2,20}$")) &&
        (surname == null || surname.matches("^[a-zA-Z-]{2,20}$")) &&
        (egn == null || egn.matches("^[0-9]{10}$"))
    ) {
      personService.updatePerson(givenName, surname, egn, personId);
      return ResponseEntity.status(200).build();
    }
    else {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping(value = "/{personId}")
  public ResponseEntity<Void> deletePerson(@PathVariable String personId)
  {
    personService.deletePerson(personId);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/{personId}")
  public ResponseEntity<Person> getPerson(@PathVariable String personId)
  {
    Person person = personService.getPersonById(personId);
    return ResponseEntity.ok(person);
  }

  @GetMapping(value = "/search")
  public List<Person> search(@RequestParam String value)
  {
    return personService.searchPeople(value);
  }

  @GetMapping(value = "/all/{page}")
  public ResponseEntity<Page<Person>> getAllPaged(@PathVariable("page") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size, @RequestParam(required = false, defaultValue = "given_name") String sort)
  {
    if (page < 0 || !sort.matches("person_id|given_name|surname|egn")) {
      return ResponseEntity.badRequest().build();
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    Page<Person> people = personService.getAllPaged(pageable);
    if (page >= people.getTotalPages()) {
      return ResponseEntity.badRequest().build();
    }
    else {
      return ResponseEntity.ok(people);
    }
  }

}