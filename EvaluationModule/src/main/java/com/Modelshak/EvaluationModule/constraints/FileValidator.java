package com.Modelshak.EvaluationModule.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

public class FileValidator implements ConstraintValidator<FileConstraint, File> {

    public static final String EXTENSION = ".uml";

    @Override
    public void initialize(FileConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(File file, ConstraintValidatorContext constraintValidatorContext) {
        String extension = file.getName().split(".")[1];
        if(EXTENSION.equals(extension)){
            return true;
        }
        return false;
    }
}
