package com.Modelshak.EvaluationModule.pojo;

public class Validation {

    private String id;
    private boolean isWarning;
    private String description;

    public Validation(String id, boolean isWarning, String description) {
        this.id = id;
        this.isWarning = isWarning;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        this.isWarning = warning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
