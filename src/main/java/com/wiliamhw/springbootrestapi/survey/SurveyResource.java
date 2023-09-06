package com.wiliamhw.springbootrestapi.survey;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public Question questionIndex(@PathVariable String surveyId, @PathVariable String questionId) {
        Question question = surveyService.findSurveyQuestionById(surveyId, questionId);

        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return question;
    }
}
