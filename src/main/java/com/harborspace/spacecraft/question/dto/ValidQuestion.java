package com.harborspace.spacecraft.question.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidQuestion {
    String message() default "Invalid Question";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
