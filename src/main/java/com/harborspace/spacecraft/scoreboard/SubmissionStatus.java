package com.harborspace.spacecraft.scoreboard;

import java.util.Arrays;

public enum SubmissionStatus {
    WAITING(0),
    COMPILATION(1),
    EXECUTION(3),
    COMPILATION_ERROR(11),
    RUNTIME_ERROR(12),
    TIME_LIMIT_EXCEEDED(13),
    SUCCESS(15),
    MEMORY_LIMIT_EXCEEDED(17),
    ILLEGAL_SYSTEM_CALL(19),
    INTERNAL_ERROR(20);

    private final int code;

    SubmissionStatus(int id) {
        this.code = id;
    }

    public static SubmissionStatus fromCode(int code) {
        return Arrays.stream(values()).filter(type -> type.code == code).findFirst().orElse(WAITING);
    }

    public int getValue() {
        return code;
    }

}
