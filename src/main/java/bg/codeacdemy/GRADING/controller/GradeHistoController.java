package bg.codeacdemy.GRADING.controller;

import bg.codeacdemy.GRADING.Service.GradeHistoService;
import bg.codeacdemy.GRADING.Service.PersonService;
import bg.codeacdemy.GRADING.bean.PersonOldGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
public class GradeHistoController {
    final
    PersonService personService;
    final
    GradeHistoService gradeHistoService;

    public GradeHistoController(PersonService personService, GradeHistoService gradeHistoService) {
        this.personService = personService;
        this.gradeHistoService = gradeHistoService;
    }

    @GetMapping(value = "{page}")
    public ResponseEntity<Page<PersonOldGrade>> getGradesPaged(@PathVariable Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                                               @RequestParam(required = false, defaultValue = "histo_dt") String sort,
                                                               @RequestParam(required = false, defaultValue = "ASC") String option) throws IllegalArgumentException {
        if (page < 0 || !sort.matches("person_id|given_name|surname|egn|grade_a|grade_b|grade_c|histo_dt|op") || !option.matches("ASC|DESC")) {
            return ResponseEntity.badRequest().build();
        }
        Sort s;
        if (option.equals("DESC"))
            s = Sort.by(sort).descending();
        else {
            s = Sort.by(sort);
        }
        Pageable pageable = PageRequest.of(page, size, s);
        Page<PersonOldGrade> histo = gradeHistoService.getHistory(pageable);

        if (page >= histo.getTotalPages()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(histo);
        }

    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<PersonOldGrade>> searchByEgn(@RequestParam String value,
                                                            @RequestParam(required = false, defaultValue = "") String date) {
        if (date.equals("") || date.matches("^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$" +
                "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$" +
                "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]" +
                "|[12][0-9]|3[01]))$|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$")) {
            List<PersonOldGrade> list = gradeHistoService.search(value, date);
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
