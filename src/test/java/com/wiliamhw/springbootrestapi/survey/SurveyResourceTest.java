package com.wiliamhw.springbootrestapi.survey;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class SurveyResourceTest {

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private MockMvc mockMvc;

    private static final String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";

    private static final String GENERIC_QUESTIONS_URI = "/surveys/Survey1/questions";

    private static final String SPECIFIC_SURVEY_URI = "/surveys/Survey1";

    private static final String GENERIC_SURVEYS_URI = "/surveys";

    @Test
    void showQuestion_404Scenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    void showQuestion_basicScenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);

        Question question = new Question("Question1", "Most Popular Cloud Platform Today",
                Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        when(surveyService.findSurveyQuestionById("Survey1", "Question1")).thenReturn(question);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        String expectedResponse = """
            {
                "id":"Question1",
                "description":"Most Popular Cloud Platform Today",
                "options":["AWS","Azure","Google Cloud","Oracle Cloud"],
                "correctAnswer":"AWS"
            }
            """;

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), true);
    }

    @Test
    void questionIndex_basicScenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(GENERIC_QUESTIONS_URI).accept(MediaType.APPLICATION_JSON);

        when(surveyService.retrieveAllSurveyQuestions("Survey1")).thenReturn(getActualQuestions());

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String expectedResponse = """
            [
                {
                    "id": "Question1",
                    "description": "Most Popular Cloud Platform Today",
                    "options": ["AWS","Azure","Google Cloud","Oracle Cloud"],
                    "correctAnswer": "AWS"
                },
                {
                    "id": "Question2",
                    "description": "Fastest Growing Cloud Platform",
                    "options": ["AWS", "Azure", "Google Cloud", "Oracle Cloud"],
                    "correctAnswer": "Google Cloud"
                }
            ]
            """;

        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), true);
    }

    @Test
    void surveyIndex_basicScenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(GENERIC_SURVEYS_URI).accept(MediaType.APPLICATION_JSON);

        List<Question> questions = getActualQuestions();
        List<Survey> surveys = List.of(new Survey(
                "Survey1", "My Favorite Survey", "Description of the Survey",
                questions
        ));

        when(surveyService.retrieveAllSurveys()).thenReturn(surveys);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String expectedResponse = """
            [
                {
                    "id": "Survey1",
                    "title": "My Favorite Survey",
                    "description": "Description of the Survey"
                }
            ]
            """;

        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
    }

    @Test
    void showSurvey_basicScenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(SPECIFIC_SURVEY_URI).accept(MediaType.APPLICATION_JSON);

        List<Question> questions = getActualQuestions();
        Survey survey = new Survey(
                "Survey1", "My Favorite Survey", "Description of the Survey",
                questions
        );

        when(surveyService.findSurveyById(survey.getId())).thenReturn(survey);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        String expectedResponse = """
            {
                "id": "Survey1",
                "title": "My Favorite Survey",
                "description": "Description of the Survey"
            }
            """;

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
    }

    @Test
    void storeSurveyQuestion_basicScenario() throws Exception {
        String requestBody = """
				{
				  "description": "Your Favorite Language",
				  "options": [
				    "Java",
				    "Python",
				    "JavaScript",
				    "Haskell"
				  ],
				  "correctAnswer": "Java"
				}
			""";

        when(surveyService.storeSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.post(GENERIC_QUESTIONS_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String locationHeader = response.getHeader("Location");

        assertEquals(201, response.getStatus());
        assertTrue(Objects.requireNonNull(locationHeader).contains("/surveys/Survey1/questions/SOME_ID"));
    }

    private List<Question> getActualQuestions() {
        return Arrays.asList(
                new Question("Question1", "Most Popular Cloud Platform Today",
                        Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS"),
                new Question("Question2",
                        "Fastest Growing Cloud Platform",
                        Arrays.asList( "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud")
        );
    }
}
