package com.harborspace.spacecraft;

import com.harborspace.spacecraft.question.dto.AnswerDto;
import com.harborspace.spacecraft.question.dto.ChoiceDto;
import com.harborspace.spacecraft.question.dto.QuestionDto;
import com.harborspace.spacecraft.question.dto.QuestionValidator;
import com.harborspace.spacecraft.question.model.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionValidatorTest {

    QuestionValidator questionValidator = new QuestionValidator();

    @Test
    void testValidMCQMulti() {
        List<ChoiceDto> choiceDtoList = List.of(
                new ChoiceDto().setSelected(true).setStatement("opt1"),
                new ChoiceDto().setSelected(true).setStatement("opt2"),
                new ChoiceDto().setSelected(false).setStatement("opt3")
        );
        AnswerDto answerDto = new AnswerDto().setChoices(choiceDtoList);
        QuestionDto questionDto = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_MULTI)
                .setAnswer(answerDto);
        assertTrue(questionValidator.isValid(questionDto, null));

    }

    @Test
    void testInvalidMCQMulti() {
        List<ChoiceDto> choiceDtoList = List.of(
                new ChoiceDto().setSelected(false).setStatement("opt1"),
                new ChoiceDto().setSelected(false).setStatement("opt2"),
                new ChoiceDto().setSelected(false).setStatement("opt3")
        );
        AnswerDto answerDto = new AnswerDto().setChoices(choiceDtoList);
        QuestionDto questionDto = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_MULTI)
                .setAnswer(answerDto);
        assertFalse(questionValidator.isValid(questionDto, null));
    }

    @Test
    void testValidMCQSingle() {
        List<ChoiceDto> choiceDtoList = List.of(
                new ChoiceDto().setSelected(true).setStatement("opt1"),
                new ChoiceDto().setSelected(false).setStatement("opt2"),
                new ChoiceDto().setSelected(false).setStatement("opt3")
        );
        AnswerDto answerDto = new AnswerDto().setChoices(choiceDtoList);
        QuestionDto questionDto = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_SINGLE)
                .setAnswer(answerDto);
        assertTrue(questionValidator.isValid(questionDto, null));
    }

    @Test
    void testInvalidMCQSingle() {
        List<ChoiceDto> choiceDtoList = List.of(
                new ChoiceDto().setSelected(true).setStatement("opt1"),
                new ChoiceDto().setSelected(true).setStatement("opt2"),
                new ChoiceDto().setSelected(false).setStatement("opt3")
        );
        AnswerDto answerDto = new AnswerDto().setChoices(choiceDtoList);
        QuestionDto questionDto = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_SINGLE)
                .setAnswer(answerDto);

        List<ChoiceDto> choiceDtoList2 = List.of(
                new ChoiceDto().setSelected(false).setStatement("opt1"),
                new ChoiceDto().setSelected(false).setStatement("opt2"),
                new ChoiceDto().setSelected(false).setStatement("opt3")
        );
        AnswerDto answerDto2 = new AnswerDto().setChoices(choiceDtoList2);
        QuestionDto questionDto2 = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_SINGLE)
                .setAnswer(answerDto2);

        assertFalse(questionValidator.isValid(questionDto2, null));
        assertFalse(questionValidator.isValid(questionDto, null));
    }

    @Test
    void testValidMCQOrder() {
        List<ChoiceDto> choiceDtoList = List.of(
                new ChoiceDto().setIndex(2).setStatement("opt1"),
                new ChoiceDto().setIndex(3).setStatement("opt2"),
                new ChoiceDto().setIndex(1).setStatement("opt3")
        );
        AnswerDto answerDto = new AnswerDto().setChoices(choiceDtoList);
        QuestionDto questionDto = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_ORDER)
                .setAnswer(answerDto);
        assertTrue(questionValidator.isValid(questionDto, null));
    }

    @Test
    void testInvalidMCQOrder() {
        List<ChoiceDto> choiceDtoList = List.of(
                new ChoiceDto().setIndex(2).setStatement("opt1"),
                new ChoiceDto().setIndex(3).setStatement("opt2"),
                new ChoiceDto().setIndex(2).setStatement("opt3")
        );
        AnswerDto answerDto = new AnswerDto().setChoices(choiceDtoList);
        QuestionDto questionDto = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_ORDER)
                .setAnswer(answerDto);

        List<ChoiceDto> choiceDtoList2 = List.of(
                new ChoiceDto().setIndex(0).setStatement("opt1"),
                new ChoiceDto().setIndex(3).setStatement("opt2"),
                new ChoiceDto().setIndex(2).setStatement("opt3")
        );
        AnswerDto answerDto2 = new AnswerDto().setChoices(choiceDtoList2);
        QuestionDto questionDto2 = new QuestionDto().setQuestionType(Question.QuestionType.MCQ_SINGLE)
                .setAnswer(answerDto2);

        assertFalse(questionValidator.isValid(questionDto2, null));
        assertFalse(questionValidator.isValid(questionDto, null));
    }

}
