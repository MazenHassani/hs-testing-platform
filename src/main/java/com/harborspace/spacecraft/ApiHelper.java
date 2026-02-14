package com.harborspace.spacecraft;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiHelper {
    public static final String API_PREFIX = "/api";
    public static final String API_COURSE_PREFIX = API_PREFIX + "/course";
    public static final String API_WEBHOOK_PREFIX = API_PREFIX + "/webhook";
    public static final String API_INVITATION_PREFIX = API_PREFIX + "/invite";
    public static final String API_QUESTION_PREFIX = API_PREFIX + "/question";
    public static final String API_TEST_QUESTION_PREFIX = API_PREFIX + "/test-question";
    public static final String API_SPHERE_ENGINE_PREFIX = API_PREFIX + "/code";
    public static final String API_CODE_QUESTION = API_PREFIX + "/code_question";
    public static final String API_SCOREBOARD_PREFIX = API_PREFIX + "/scoreboard";
    public static final String API_MAIL_PREFIX = API_PREFIX + "/mail";
    public static final String API_SUBMISSION_PREFIX = API_PREFIX + "/submission";
}
