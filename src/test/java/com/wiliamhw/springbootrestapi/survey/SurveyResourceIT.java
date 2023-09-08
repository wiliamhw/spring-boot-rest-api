package com.wiliamhw.springbootrestapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

    private static final String SPECIFIC_QUESTION_URI = "/surveys/Survey1/questions/Question1";

    private static final String GENERIC_QUESTIONS_URI = "/surveys/Survey1/questions";

    private static final String SPECIFIC_SURVEY_URI = "/surveys/Survey1";

    private static final String GENERIC_SURVEYS_URI = "/surveys";

    @Autowired
    private TestRestTemplate template;

    @Test
    void showQuestion_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URI, String.class);

        String expectedResponse = """
            {
                "id":"Question1",
                "description":"Most Popular Cloud Platform Today",
                "correctAnswer":"AWS"
            }
        """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void questionIndex_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_QUESTIONS_URI, String.class);

        String expectedResponse = """
            [
                {
                    "id": "Question1"
                },
                {
                    "id": "Question2"
                },
                {
                    "id": "Question3"
                }
            ]
        """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void surveyIndex_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_SURVEYS_URI, String.class);

        String expectedResponse = """
            [
                {
                    "id": "Survey1",
                    "questions": [
                        {
                            "id": "Question1"
                        },
                        {
                            "id": "Question2"
                        },
                        {
                            "id": "Question3"
                        }
                    ]
                }
            ]
        """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void showSurvey_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_SURVEY_URI, String.class);

        String expectedResponse = """
            {
                "id": "Survey1",
                "questions": [
                    {
                        "id": "Question1"
                    },
                    {
                        "id": "Question2"
                    },
                    {
                        "id": "Question3"
                    }
                ]
            }
        """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void storeQuestion_basicScenario() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity
                = template.exchange(GENERIC_QUESTIONS_URI, HttpMethod.POST, httpEntity, String.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));

        template.delete(locationHeader);
    }
}
