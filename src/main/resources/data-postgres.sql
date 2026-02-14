INSERT INTO course (name, start_date, end_date)
SELECT 'Testing Course', TO_DATE('2021-07-14', 'yyyy-MM-dd'), TO_DATE('2021-07-20', 'yyyy-MM-dd')
 WHERE NOT EXISTS (SELECT 1 FROM course WHERE name = 'Testing Course')
 ON CONFLICT DO NOTHING;

INSERT INTO test (name, course_id, start_date, end_date, duration)
SELECT 'Experimental Test', '1', TO_DATE('14-07-2020', 'dd-MM-yyyy'), TO_DATE('20-08-2020', 'dd-MM-yyyy'), 100800
WHERE NOT EXISTS (SELECT 1 FROM test WHERE name = 'Experimental Test')
ON CONFLICT DO NOTHING;

INSERT INTO question (title, widget_code, question_type, creator)
SELECT 'Hello World', 'Xq0FQitT3M', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819'
WHERE NOT EXISTS (SELECT 1 FROM question WHERE widget_code = 'Xq0FQitT3M')
ON CONFLICT DO NOTHING;

INSERT INTO question (title, widget_code, question_type, creator)
SELECT 'N + 2', 'kMgujaFFUe', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819'
WHERE NOT EXISTS (SELECT 1 FROM question WHERE widget_code = 'kMgujaFFUe')
ON CONFLICT DO NOTHING;

INSERT INTO question (title, widget_code, question_type, creator)
SELECT 'A + B', 'vPZI7a7lMS', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819'
WHERE NOT EXISTS (SELECT 1 FROM question WHERE widget_code = 'vPZI7a7lMS')
ON CONFLICT DO NOTHING;

INSERT INTO question (title, widget_code, question_type, creator)
SELECT 'Sort Three Numbers', 'wcACiToerE', 1, '1d2cae76-0eeb-4965-b58c-524ad2726819'
WHERE NOT EXISTS (SELECT 1 FROM question WHERE widget_code = 'wcACiToerE')
ON CONFLICT DO NOTHING;

INSERT INTO test_question (question_id, test_id, weight, position) VALUES (1, 1, 1,0) ON CONFLICT DO NOTHING;
INSERT INTO test_question (question_id, test_id, weight, position) VALUES (2, 1, 1,1) ON CONFLICT DO NOTHING;
INSERT INTO test_question (question_id, test_id, weight, position) VALUES (3, 1, 1,2) ON CONFLICT DO NOTHING;
INSERT INTO test_question (question_id, test_id, weight, position) VALUES (4, 1, 1,3) ON CONFLICT DO NOTHING;
