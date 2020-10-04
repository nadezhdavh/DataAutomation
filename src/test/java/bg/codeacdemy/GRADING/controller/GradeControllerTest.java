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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class GradeControllerTest extends AbstractTestNGSpringContextTests
{
  @SuppressWarnings("unused")

  private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation();

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  FieldDescriptor[] personGrade = new FieldDescriptor[]{
      fieldWithPath("givenName").description("The first name of the person."),
      fieldWithPath("surname").description("The last name of the person."),
      fieldWithPath("gradeA").description("The person's first grade from the table 'GRADE'."),
      fieldWithPath("gradeB").description("The person's second grade from the table 'GRADE'."),
      fieldWithPath("gradeC").description("The person's first third from the table 'GRADE'.")};

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
  public void testAddGrade() throws Exception
  {
    this.mockMvc.perform(post("/api/v1/grades?id=ID00000141&gradeA=A&gradeB=B&gradeC=C"))
        .andExpect(status().isCreated())
        .andDo(document("addGrade",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("id").description(" The ID of the person."),
                parameterWithName("gradeA").description("Fist grade."),
                parameterWithName("gradeB").description("Second grade."),
                parameterWithName("gradeC").description("Third grade."))));
  }

  @Test
  public void testUpdateGrade() throws Exception
  {
    this.mockMvc.perform(patch("/api/v1/grades/{personId}?gradeA=C&gradeB=A&gradeC=B", "ID00000039"))
        .andExpect(status().isOk())
        .andDo(document("updateGrade",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("personId").description("Id of the person.")),
            requestParameters(
                parameterWithName("gradeA").description("Fist grade."),
                parameterWithName("gradeB").description("Second grade."),
                parameterWithName("gradeC").description("Third grade."))));
  }

  @Test
  public void testDeleteGrade() throws Exception
  {

    this.mockMvc.perform(delete("/api/v1/grades/{personId}", "ID00000040"))
        .andExpect(status().isOk())
        .andDo(document("deleteGrade",
            pathParameters(parameterWithName("personId").description("The id of the person whose grades will be deleted."))));
  }

  @Test
  public void testGetOne() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/grades/{personId}", "ID00000055").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("getGrade",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("personId").description("The ID of the person."))));
  }

  @Test
  public void testSearch() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/grades/search/{page}/?size=2&gradeA=A&gradeB=B&gradeC=A", 0).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("searchGrade",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page."),
                parameterWithName("gradeA").description("Fist grade."),
                parameterWithName("gradeB").description("Second grade."),
                parameterWithName("gradeC").description("Third grade."))));
  }

  @Test
  public void testSearchFail() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/grades/search/{page}/?size=2&gradeA=A&gradeB=B&gradeC=A", 200).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andDo(document("searchGradeFail",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page."),
                parameterWithName("gradeA").description("Fist grade."),
                parameterWithName("gradeB").description("Second grade."),
                parameterWithName("gradeC").description("Third grade."))));
  }

  @Test
  public void testGetGradesPaged() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/grades/all/{page}/?size=2&sort=egn&option=DESC", 1).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("getGradePaged",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page."),
                parameterWithName("sort").description("Sort list by column name."),
                parameterWithName("option").description("Sort ASC or DESC."))));
  }

  @Test
  public void testGetGradesPagedFail() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/grades/all/{page}/?size=2&sort=fail&option=DESC", 1).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andDo(document("getGradePagedFail",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page."),
                parameterWithName("sort").description("Sort list by column name."),
                parameterWithName("option").description("Sort ASC or DESC."))));
  }
}