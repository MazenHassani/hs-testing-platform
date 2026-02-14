INSERT INTO course (name, start_date, end_date)
VALUES ('Blockchain', TO_DATE('2020-03-15', 'yyyy-MM-dd'), TO_DATE('2020-04-15', 'yyyy-MM-dd'));

INSERT INTO course (name, start_date, end_date)
VALUES ('Fintech 2.0', TO_DATE('2020-04-15', 'yyyy-MM-dd'), TO_DATE('2020-05-15', 'yyyy-MM-dd'));
INSERT INTO course (name, start_date, end_date)
VALUES ('From Zero to Hero', TO_DATE('2020-04-15', 'yyyy-MM-dd'), TO_DATE('2020-05-15', 'yyyy-MM-dd'));
INSERT INTO course (name, start_date, end_date)
VALUES ('Programming Interactivity II', TO_DATE('2020-04-15', 'yyyy-MM-dd'), TO_DATE('2020-05-15', 'yyyy-MM-dd'));
INSERT INTO course (name, start_date, end_date)
VALUES ('Data Visualisation', TO_DATE('2020-04-15', 'yyyy-MM-dd'), TO_DATE('2020-05-15', 'yyyy-MM-dd'));
INSERT INTO course (name, start_date, end_date)
VALUES ('Statistical Data Analysis', TO_DATE('2020-04-15', 'yyyy-MM-dd'), TO_DATE('2020-05-15', 'yyyy-MM-dd'));
INSERT INTO course (name, start_date, end_date)
VALUES ('Discrete Mathematics 1.0  ', TO_DATE('2020-04-15', 'yyyy-MM-dd'), TO_DATE('2020-05-15', 'yyyy-MM-dd'));
INSERT INTO course (name, start_date, end_date)
VALUES ('Future Course', TO_DATE('2021-04-15', 'yyyy-MM-dd'), TO_DATE('2021-05-15', 'yyyy-MM-dd'));

INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('test1', '1', TO_DATE('30-06-2020', 'dd-MM-yyyy'), TO_DATE('30-08-2020', 'dd-MM-yyyy'), 60);


INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('test2', '1', TO_DATE('15-04-2021', 'dd-MM-yyyy'), TO_DATE('16-04-2021', 'dd-MM-yyyy'), 60);


INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('test3', '2', TO_DATE('13-03-2019 13:25:01', 'dd-MM-yyyy HH24:MI:SS'), TO_DATE('15-04-2019 05:17:55',
                                                                                       'dd-MM-yyyy ' ||
                                                                                       'HH24:MI:SS'), 60);


INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('MyTest', '1', TO_DATE('13-05-2020 13:25:01', 'dd-MM-yyyy HH24:MI:SS'), TO_DATE('14-05-2020 05:17:55',
                                                                                        'dd-MM-yyyy ' ||
                                                                                        'HH24:MI:SS'), 60);
INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('assignment', '1', TO_DATE('15-04-2020', 'dd-MM-yyyy'), TO_DATE('16-04-2020', 'dd-MM-yyyy'), 60);

INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('project', '1', TO_DATE('01-01-2020', 'dd-MM-yyyy'), TO_DATE('07-01-2021', 'dd-MM-yyyy'), 60);

INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('side project', '1', TO_DATE('01-02-2020', 'dd-MM-yyyy'), TO_DATE('01-06-2020', 'dd-MM-yyyy'), 60);


INSERT INTO test (name, course_id, start_date, end_date, duration)
VALUES ('capstone', '1', TO_DATE('09-02-2020', 'dd-MM-yyyy'), TO_DATE('10-01-2020', 'dd-MM-yyyy'), 60);


INSERT INTO question (title, widget_code, question_type, creator)
VALUES ('Sphere 1', 'abc', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819');
INSERT INTO question (title, statement, question_type, creator)
VALUES ('Lesson 1', 'Lesson 1 binary search', 0, '1d2cae76-0eeb-4965-b58c-524ad2726819');
INSERT INTO question (title, widget_code, question_type, creator)
VALUES ('Down with the comments', 'x3bN5XxeFf', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819');
INSERT INTO question (title, widget_code, question_type, creator)
VALUES ('Lexicographical order', 'ckLCFPbYGs', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819');
INSERT INTO question (title, statement, question_type, creator)
VALUES ('Lesson 2: Binary search', 'This is a lesson about binary search\n$f(x) = ax^2 + bx + c$ and $md = ' ||
                                   '\frac{low + hight}{2}$\n<iframe width="560" height="315"' ||
                                   ' src="https://www.youtube-nocookie.com/embed/P3YID7liBug?controls=0" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>',
        0, '1d2cae76-0eeb-4965-b58c-524ad2726819');
INSERT INTO question (title, statement, question_type, creator)
VALUES ('Lesson 3: LiChao Tree', 'This is a lesson about LiChao Tree', 0, '1d2cae76-0eeb-4965-b58c-524ad2726819');


INSERT INTO answer (id)
VALUES (20);
INSERT INTO answer (id)
VALUES (21);
INSERT INTO answer (id)
VALUES (22);

INSERT INTO answer_choice (answer_id, statement, selected)
VALUES (20, 'opt1', 1);
INSERT INTO answer_choice (answer_id, statement, selected)
VALUES (20, 'opt2', 0);
INSERT INTO answer_choice (answer_id, statement, selected)
VALUES (20, 'opt3', 1);

INSERT INTO answer_choice (answer_id, statement, index)
VALUES (21, 'opt1', 1);
INSERT INTO answer_choice (answer_id, statement, index)
VALUES (21, 'opt2', 2);
INSERT INTO answer_choice (answer_id, statement, index)
VALUES (21, 'opt3', 3);

INSERT INTO answer_choice (answer_id, statement, selected)
VALUES (22, 'opt1', 1);
INSERT INTO answer_choice (answer_id, statement, selected)
VALUES (22, 'opt2', 0);
INSERT INTO answer_choice (answer_id, statement, selected)
VALUES (22, 'opt3', 0);

INSERT INTO answer_number (id, value, precision)
VALUES (1, 11.05, 1e-7);
INSERT INTO answer (id, answer_number_id)
VALUES (23, 1);

INSERT INTO question (id, title, statement, question_type, creator, answer_id)
VALUES (22, 'MCQ Multi', 'Choose 1+ of the options', 2, '1d2cae76-0eeb-4965-b58c-524ad2726819', 20);
INSERT INTO question (id, title, statement, question_type, creator, answer_id)
VALUES (24, 'MCQ Order', 'Order these options', 4, '1d2cae76-0eeb-4965-b58c-524ad2726819', 21);
INSERT INTO question (id, title, statement, question_type, creator, answer_id)
VALUES (23, 'MCQ Single','Choose exactly 1 of these options', 3, '1d2cae76-0eeb-4965-b58c-524ad2726819', 22);
INSERT INTO question (id, title, statement, question_type, creator, answer_id)
VALUES (25, 'Number', 'Reply with a number', 5, '1d2cae76-0eeb-4965-b58c-524ad2726819', 23);


MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (1, 5, 1, 1,0);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (2, 3, 1, 1,1);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (3, 4, 1, 1,3);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (4, 6, 1, 1,2);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (5, 22, 1, 1,4);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (6, 23, 1, 1,5);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (7, 25, 1, 1,6);
MERGE INTO test_question (id, question_id, test_id, weight, position) VALUES (8, 24, 1, 1,7);

MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status) VALUES ( 1, 1, 'kee', 2, 2 );
MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status) VALUES ( 2, 2, 'ibrahim', 2, 0 );
MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status, is_accepted, score)
VALUES ( 3, 3, 'ibrahim', 3, 6, true, 0);
MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status, is_accepted, score)
VALUES ( 4, 4, 'ibrahim', 2, 6, true,20);
MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status, is_accepted, score)
VALUES ( 5, 5, 'john', 3, 6, true, 100);
MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status, is_accepted, score)
VALUES ( 6, 6, 'john', 3, 7, true, 0);
MERGE INTO submission (id, submission_api_id, user_id, test_question_id, status, is_accepted, score)
VALUES ( 7, 7, 'kee', 2, 6, true, 100);


INSERT INTO user_question_score (test_question_id, user_id, best_submission_id) values ( 3, 'ibrahim', 3);
INSERT INTO user_question_score (test_question_id, user_id, best_submission_id) values ( 1, 'kee', 7);
INSERT INTO user_question_score (test_question_id, user_id, best_submission_id) values ( 3, 'john', 5);
INSERT INTO user_question_score (test_question_id, user_id, best_submission_id) values ( 1, 'ibrahim', 4);


INSERT INTO student_submission (student_id, val, task_id)
VALUES ('kee', 'opt2', 5);
INSERT INTO student_submission (student_id, val, task_id)
VALUES ('john', 'opt2', 5);
INSERT INTO student_submission (student_id, val, task_id)
VALUES ('kee', '11.05', 7);
INSERT INTO student_submission (student_id, val, task_id)
VALUES ('john', '11.0500001', 7);
INSERT INTO student_submission (student_id, val, task_id)
VALUES ('kee', 'opt2', 8);
INSERT INTO student_submission (student_id, val, task_id)
VALUES ('john', 'opt2', 8);