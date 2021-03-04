package com.Modelshak.EvaluationModule.pojo;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private List<Validation> errors = new ArrayList<>();
    private List<Validation> warnings = new ArrayList<>();


    public List<Validation> getErrors() {
        return errors;
    }

    public List<Validation> getWarnings() {
        return warnings;
    }

}
