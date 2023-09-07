package com.wiliamhw.springbootrestapi.survey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SurveyResource {
    private SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        super();
        this.surveyService = surveyService;
    }

    @RequestMapping("/surveys")
    public List<Survey> surveyIndex() {
        return surveyService.retrieveAllSurveys();
    }

    @RequestMapping("/surveys/{surveyId}")
    public Survey showSurvey(@PathVariable String surveyId) {
        Survey survey = surveyService.findSurveyById(surveyId);

        if (survey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return survey;
    }

    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> questionIndex(@PathVariable String surveyId) {
        List<Question> questions = surveyService.retrieveAllSurveyQuestions(surveyId);

        if (questions == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return questions;
    }

    @RequestMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question showQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        Question question = surveyService.findSurveyQuestionById(surveyId, questionId);

        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return question;
    }

    @RequestMapping(value = "/surveys/{surveyId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Object> storeQuestion(@PathVariable String surveyId, @RequestBody Question question) {
        String questionId = surveyService.storeSurveyQuestion(surveyId, question);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{questionId}").buildAndExpand(questionId).toUri();

        return ResponseEntity.created(location).build();
    }
}
