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

import java.util.Base64;

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
        HttpHeaders headers = createHttpAuthHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity
                = template.exchange(SPECIFIC_QUESTION_URI, HttpMethod.GET, httpEntity, String.class);

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
        HttpHeaders headers = createHttpAuthHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity
                = template.exchange(GENERIC_QUESTIONS_URI, HttpMethod.GET, httpEntity, String.class);

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
        HttpHeaders headers = createHttpAuthHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity
                = template.exchange(GENERIC_SURVEYS_URI, HttpMethod.GET, httpEntity, String.class);

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
        HttpHeaders headers = createHttpAuthHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity
                = template.exchange(SPECIFIC_SURVEY_URI, HttpMethod.GET, httpEntity, String.class);

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

        HttpHeaders headers = createHttpAuthHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity
                = template.exchange(GENERIC_QUESTIONS_URI, HttpMethod.POST, httpEntity, String.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));

        template.delete(locationHeader);
    }

    private HttpHeaders createHttpAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic " + performBasicAuthEncoding("admin", "password"));
        return headers;
    }


    private String performBasicAuthEncoding(String user, String password) {
        String combined = user + ":" + password;
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        return new String(encodedBytes);
    }
}
