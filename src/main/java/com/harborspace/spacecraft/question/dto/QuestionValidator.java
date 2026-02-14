package com.harborspace.spacecraft.question.dto;

import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.model.Question.QuestionType;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class QuestionValidator implements ConstraintValidator<ValidQuestion, QuestionDto> {

    @Override
    public void initialize(ValidQuestion constraintAnnotation) {
    }

    @Override
    public boolean isValid(QuestionDto questionDto, ConstraintValidatorContext constraintValidatorContext) {

        Question.QuestionType type = questionDto.getQuestionType();
        if (questionDto.getAnswer() == null)
            return true;

        switch (type) {
            case MCQ_MULTI:
            case MCQ_SINGLE:
                return isValidMcq(questionDto);
            case NUMBER:
                return isValidNumber(questionDto);
            case MCQ_ORDER:
                return isValidOrder(questionDto);
            default:
                break;
        }
        return true;
    }

    private boolean isValidNumber(QuestionDto questionDto) {
        List<ChoiceDto> choiceList = questionDto.getAnswer().getChoices();
        NumberDto number = questionDto.getAnswer().getNumber();
        return number != null && choiceList == null;
    }

    private boolean isValidOrder(QuestionDto questionDto) {
        List<ChoiceDto> choiceList = questionDto.getAnswer().getChoices();
        NumberDto number = questionDto.getAnswer().getNumber();
        if (choiceList == null || number != null || choiceList.size() < 2)
            return false;

        HashSet<Integer> rangeSet = new HashSet<>();
        IntStream.range(1, choiceList.size() + 1).forEach(rangeSet::add);
        for (ChoiceDto choice : choiceList) {
            if (choice.getIndex() == null || choice.getSelected() != null)
                return false;
            if (rangeSet.contains(choice.getIndex()))
                rangeSet.remove(choice.getIndex());
            else return false;
        }
        return true;
    }

    private boolean isValidMcq(QuestionDto questionDto) {
        QuestionType type = questionDto.getQuestionType();
        List<ChoiceDto> choiceList = questionDto.getAnswer().getChoices();
        NumberDto number = questionDto.getAnswer().getNumber();
        if (choiceList == null || number != null || choiceList.size() < 2)
            return false;

        int correct = 0;
        for (ChoiceDto choice : choiceList) {
            if (choice.getIndex() != null || choice.getSelected() == null)
                return false;
            if (choice.getSelected())
                correct += 1;
        }
        return (correct != 0) && (type != QuestionType.MCQ_SINGLE || correct <= 1);
    }
}