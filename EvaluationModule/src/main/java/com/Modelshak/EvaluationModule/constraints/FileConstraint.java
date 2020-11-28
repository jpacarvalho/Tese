package com.Modelshak.EvaluationModule.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.LOCAL_VARIABLE,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileConstraint {
    String message() default
            "Calendar not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
