package com.wiliamhw.springbootrestapi.survey;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class SurveyService {
    private static List<Survey> surveys = new ArrayList<>();

    static {

        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey",
                "Description of the Survey", questions);

        surveys.add(survey);


    }

    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    public Survey findById(String id) {
        Predicate<? super Survey> predicate = survey -> Objects.equals(survey.getId(), id);

        Optional<Survey> optionalSurvey
                = surveys.stream().filter(predicate).findFirst();

        return optionalSurvey.orElse(null);
    }
}
