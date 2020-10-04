package bg.codeacdemy.GRADING.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class GradeHistoControllerTest extends AbstractTestNGSpringContextTests
{
  @SuppressWarnings("unused")

  private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation();

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  FieldDescriptor[] personHisto = new FieldDescriptor[]{
      fieldWithPath("givenName").description("The first name of the person."),
      fieldWithPath("surname").description("The last name of the person."),
      fieldWithPath("egn").description("The EGN of the person."),
      fieldWithPath("gradeA").description("The person's first grade from the table 'GRADE'."),
      fieldWithPath("gradeB").description("The person's second grade from the table 'GRADE'."),
      fieldWithPath("gradeC").description("The person's first third from the table 'GRADE'."),
      fieldWithPath("date").description("The date when update or delete is performed."),
      fieldWithPath("option").description("'DEL' if delete is performed, or 'UPD' if update.")};

  @BeforeMethod
  public void setUp(Method method)
  {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(this.restDocumentation)).build();
    this.restDocumentation.beforeTest(getClass(), method.getName());
  }

  @AfterMethod
  public void tearDown()
  {
    this.restDocumentation.afterTest();
  }

  @Test
  public void testGetGradesPaged() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/history/{page}?size=2&sort=histo_dt&option=DESC", 1).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("getHistory",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page - optional, default 10."),
                parameterWithName("sort").description("Column name that the array is sorted by - optional, default histo_dt"),
                parameterWithName("option").description("Sort option - optional, default ASC"))));
  }

  @Test
  public void testGetGradesPagedFail() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/history/{page}?size=2&sort=fail&option=ASC", 1).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andDo(document("getHistoryFail",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page - optional, default 10."),
                parameterWithName("sort").description("Column name that the array is sorted by - optional, default histo_dt"),
                parameterWithName("option").description("Sort option - optional, default ASC"))));
  }

  @Test
  public void testSearch() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/history/search?value=ID00000033&date=2020-07-20").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("searchHistory",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("value").description("The search parameter is EGN or ID of the person."),
                parameterWithName("date").description("The date when a grade for the person is updated or deleted.")),
            responseFields(
                fieldWithPath("[]").description("An array of people with their old grades."))
                .andWithPrefix("[].", personHisto)));
  }

  @Test
  public void testSearchFail() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/history/search?value=ID00000033&date=2020-07").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andDo(document("searchHistoryFail",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("value").description("The search parameter is EGN or ID of the person."),
                parameterWithName("date").description("The date when a grade for the person is updated or deleted."))));
  }
}