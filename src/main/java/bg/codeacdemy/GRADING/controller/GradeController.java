package bg.codeacdemy.GRADING.controller;

import bg.codeacdemy.GRADING.Service.GradeService;
import bg.codeacdemy.GRADING.bean.PersonGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/v1/grades")
public class GradeController
{
  final
  GradeService gradeService;

  public GradeController(GradeService gradeService)
  {
    this.gradeService = gradeService;
  }

  @GetMapping(value = "/{personId}")
  public ResponseEntity<PersonGrade> getGrade(@PathVariable("personId") String personId)
  {
    PersonGrade p = gradeService.getPGradeById(personId);
    return ResponseEntity.ok(p);
  }

  @PostMapping()
  public ResponseEntity<Void> addGrade(
      @RequestParam @Pattern(regexp = "^[A-G]$") String gradeA,
      @RequestParam @Pattern(regexp = "^[A-G]$") String gradeB,
      @RequestParam @Pattern(regexp = "^[A-G]$") String gradeC,
      @RequestParam String id)
  {
    gradeService.addGrade(id, gradeA, gradeB, gradeC);
    return ResponseEntity.status(HttpStatus.CREATED).build();//??
  }

  @PatchMapping(value = "/{personId}")
  public ResponseEntity<Void> updateGrade(@PathVariable("personId") String personId,
                                          @RequestParam(required = false) String gradeA,
                                          @RequestParam(required = false) String gradeB,
                                          @RequestParam(required = false) String gradeC)
  {
    if ((gradeA == null || gradeA.matches("^[A-G]$")) &&
        (gradeB == null || gradeB.matches("^[A-G]$")) &&
        (gradeC == null || gradeC.matches("^[A-G]$"))
    ) {
      gradeService.updateGrade(personId, gradeA, gradeB, gradeC);
      return ResponseEntity.ok().build();
    }
    else {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping(value = "/{personId}")
  public ResponseEntity<Void> deleteGrade(@PathVariable("personId") String personId)
  {
    gradeService.deleteGrade(personId);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/search/{page}")
  public ResponseEntity<Page<PersonGrade>> search(
      @PathVariable Integer page, @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false) String gradeA,
      @RequestParam(required = false) String gradeB,
      @RequestParam(required = false) String gradeC
  )
  {
    Pageable pageable = PageRequest.of(page, size, Sort.by("grade_a").and(Sort.by("grade_b").and(Sort.by("grade_c"))));
    Page<PersonGrade> personGrades = gradeService.search(gradeA, gradeB, gradeC, pageable);

    if (page >= personGrades.getTotalPages()) {
      return ResponseEntity.badRequest().build();
    }

    if ((gradeA == null) || (gradeA.matches("^[A-G]$")) &&
        (gradeB == null) || (gradeB.matches("^[A-G]$")) &&
        (gradeC == null) || (gradeC.matches("^[A-G]$"))) {
      return ResponseEntity.ok(personGrades);

    }
    else {
      return ResponseEntity.badRequest().build();
    }

  }

  @GetMapping(value = "/all/{page}")
  public ResponseEntity<Page<PersonGrade>> getGradesPaged(
      @PathVariable Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "given_name") String sort,
      @RequestParam(required = false, defaultValue = "ASC") String option)
  {
    Sort s;
    if (page < 0 || !sort.matches("person_id|given_name|surname|egn|grade_a|grade_b|grade_c") || !option.matches("ASC|DESC")) {
      return ResponseEntity.badRequest().build();
    }
    if (option.equals("DESC")) {
      s = Sort.by(sort).descending();
    }
    else {
      s = Sort.by(sort);
    }
    Pageable pageable = PageRequest.of(page, size, s);
    Page<PersonGrade> personGrades = gradeService.findAll(pageable);
    if (page >= personGrades.getTotalPages()) {
      return ResponseEntity.badRequest().build();
    }
    else {
      return ResponseEntity.ok(personGrades);
    }

  }

}