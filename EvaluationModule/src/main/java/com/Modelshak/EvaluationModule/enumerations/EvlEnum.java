package com.Modelshak.EvaluationModule.enumerations;

public enum EvlEnum {
    USEEVL("src/main/resources/validateUse.evl"),
    CLASSEVL("src/main/resources/validateClass.evl"),
    ACTIVITYEVL("src/main/resources/validateActivity.evl"),
    STATEEVL( "src/main/resources/validateState.evl");
    //SEQUENCEEVL("src/main/resources/validateSequence.evl");

    private final String path;
    EvlEnum(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
