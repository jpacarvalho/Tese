package com.Modelshak.EvaluationModule.pojo;

import java.util.List;

public class Request {

    private List<Project> projects;
    private List<ReceivedValidations> validations;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<ReceivedValidations> getValidations() {
        return validations;
    }

    public void setValidations(List<ReceivedValidations> validations) {
        this.validations = validations;
    }
}
