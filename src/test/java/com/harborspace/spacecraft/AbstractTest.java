package com.harborspace.spacecraft;

import com.harborspace.spacecraft.course.model.Course;
import com.harborspace.spacecraft.course.repository.CourseRepository;
import com.harborspace.spacecraft.course.repository.TestRepository;
import com.harborspace.spacecraft.question.model.Question;
import com.harborspace.spacecraft.question.repository.QuestionRepository;
import com.harborspace.spacecraft.scoreboard.model.Submission;
import com.harborspace.spacecraft.scoreboard.repository.SubmissionRepository;
import com.harborspace.spacecraft.scoreboard.repository.UserQuestionScoreRepository;
import com.harborspace.spacecraft.testquestion.model.TestQuestion;
import com.harborspace.spacecraft.testquestion.repository.TestQuestionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.CollectionUtils;

import static java.lang.Thread.sleep;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {
    public static final int EXSIST_PROBLEM_ID = 34932;
    public static final int DEFAULT_COMPILER_ID = 1;
    public static final int FINAL_STATUS_BOUNDARY = 10;
    public static final String STUDENT_ID = "studentId1";
    public static final String CODE_DEFAULT = "<int main()\n" +
            "    {\n" +
            "        printf(\"Hello\");\n" +
            "        return 0;\n" +
            "    }>";

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected UserQuestionScoreRepository userQuestionScoreRepository;

    @Autowired
    protected SubmissionRepository submissionRepository;

    @Autowired
    protected TestRepository testRepository;

    @Autowired
    protected CourseRepository courseRepository;

    @Autowired
    protected TestQuestionRepository testQuestionRepository;

    protected List<Question> questionList = new ArrayList<>();

    protected List<Submission> submissions = new ArrayList<>();

    protected List<com.harborspace.spacecraft.course.model.Test> testList = new ArrayList<>();

    protected List<TestQuestion> testQuestions = new ArrayList<>();

    protected Course course;

    protected Question initQuestion(String title, String creator) {
        Question question;
        var questions = questionRepository.findAllByCreator(creator);
        if (!CollectionUtils.isEmpty(questions)) {
            question = questions.get(0);
        } else {
            question = new Question();
            question.setTitle(title);
            question.setCreator(creator);
            questionRepository.save(question);
            questionList.add(question);
        }
        return question;
    }

    protected com.harborspace.spacecraft.course.model.Test initTest(String title, Course course) {
        com.harborspace.spacecraft.course.model.Test test = new com.harborspace.spacecraft.course.model.Test();
        test.setName(title);
        test.setCourse(course);
        testRepository.save(test);
        testList.add(test);
        return test;
    }

    protected TestQuestion initTestQuestion(com.harborspace.spacecraft.course.model.Test test, Question question) {
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setTest(test);
        testQuestion.setTestId(test.getId());
        testQuestion.setQuestion(question);
        testQuestion.setQuestionId(question.getId());
        testQuestion.setWeight(1);
        testQuestionRepository.save(testQuestion);
        testQuestions.add(testQuestion);
        return testQuestion;
    }

    protected <T> void clear(T entity, CrudRepository<T, Long> repository) {
        if (repository != null && entity != null) {
            log.info(entity.getClass().getName() + " before clear:" + repository.count());
            repository.delete(entity);
            log.info(entity.getClass().getName() + " after clear:" + repository.count());
        }
    }

    protected <T> void clear(List<T> entities, CrudRepository<T, Long> repository) {
        if (repository != null && !CollectionUtils.isEmpty(entities)) {
            log.info(entities.get(0).getClass().getCanonicalName() + " before clear:" + repository.count());
            entities.forEach(repository::delete);
            log.info(entities.get(0).getClass().getCanonicalName() + " after clear:" + repository.count());
        }
    }

    protected void awaitAsyncProcessing(int delay) {
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    protected void initDefaultObject() {
        var question = initQuestion("testQuestion1", "testCreator1");
        question.setQuestionType(Question.QuestionType.SPHERE);
        if (!questionList.contains(question)) {
            questionList.add(question);
        }

        if (course == null) {
            course = new Course();
            course.setName("course");
            courseRepository.save(course);
        }

        if (CollectionUtils.isEmpty(testList)) {
            initTest("test", course);
        }

        if (CollectionUtils.isEmpty(testQuestions)) {
            initTestQuestion(testList.get(0), question);
        }
    }

    protected void clearTestObject() {
        log.info("clear start");
        if (!CollectionUtils.isEmpty(testQuestions)) {
            testQuestions.forEach(testQuestion -> userQuestionScoreRepository
                    .findByTestQuestionTestId(testQuestion.getTestId())
                    .forEach(userQuestionScore -> clear(userQuestionScore, userQuestionScoreRepository))
            );
            clear(submissions, submissionRepository);
            clear(testQuestions, testQuestionRepository);
            clear(questionList, questionRepository);
            clear(testList, testRepository);
            clear(course, courseRepository);
        }
        log.info("clear end");
    }
}
