package bg.codeacdemy.GRADING.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.ManualRestDocumentation;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PersonControllerTest extends AbstractTestNGSpringContextTests
{
  @SuppressWarnings("unused")

  private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation();

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

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
  public void testAddPerson() throws Exception
  {
    String egn = RandomStringUtils.randomNumeric(10);
    this.mockMvc.perform(post("/api/v1/people?givenName=Ivana&surname=No&egn=" + egn + ""))
        .andExpect(status().isCreated())
        .andDo(document("addPerson",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("givenName").description("First name of the person."),
                parameterWithName("surname").description("Last name of the person."),
                parameterWithName("egn").description("Search parameter is EGN."))));
  }

  @Test
  public void testUpdatePerson() throws Exception
  {
    this.mockMvc.perform(patch("/api/v1/people/{personId}?givenName=Iva&surname=Nikolova&egn=9007010000", "ID00000141"))
        .andExpect(status().isOk())
        .andDo(document("updatePerson",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("personId").description("Id of the person.")),
            requestParameters(
                parameterWithName("givenName").description("First name of the person."),
                parameterWithName("surname").description("Last name of the person."),
                parameterWithName("egn").description("Search parameter is EGN."))));
  }

  @Test
  public void testDeletePerson() throws Exception
  {
    this.mockMvc.perform(delete("/api/v1/people/{personId}", "ID00000123"))
        .andExpect(status().isOk())
        .andDo(document("deletePerson",
            pathParameters(parameterWithName("personId").description("The id of the person."))));
  }

  @Test
  public void testSearch() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/people/search?value=Angel").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("searchPeople",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("value").description("Search by some value as it may be character," +
                    " word or number - it check columns Given Name OR Surname OR EGN in person table."))));
  }

  @Test
  public void testGetAllPaged() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/people/all/{page}?size=3&sort=egn", 1).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("getPeoplePaged",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page."),
                parameterWithName("sort").description("Sort list by given_name OR surname OR egn"))));
  }

  @Test
  public void testGetAllPagedFail() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/people/all/{page}?size=3&sort=fail", 1).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andDo(document("getPeoplePagedFail",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("page").description("The page to retrieve.")),
            requestParameters(
                parameterWithName("size").description("Entries per page."),
                parameterWithName("sort").description("Sort list by given_name OR surname OR egn"))));
  }

  @Test
  public void testGetOne() throws Exception
  {
    this.mockMvc.perform(get("/api/v1/people/{personId}", "ID00000141").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("getPerson",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("personId").description("The ID of the person."))));
  }
}