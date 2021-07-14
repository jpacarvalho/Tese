package com.Modelshak.EvaluationModule.enumerations;

public enum EolEnum {
    USEEOL("src/main/resources/countUse.eol"),
    //CLASSEOL("src/main/resources/countClass.eol"),
    ACTIVITYEOL("src/main/resources/countActivity.eol");
    //STATEEOL( "src/main/resources/countState.eol"),
    //SEQUENCEEOL("src/main/resources/countSequence.eol");

    private final String path;
    EolEnum(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
