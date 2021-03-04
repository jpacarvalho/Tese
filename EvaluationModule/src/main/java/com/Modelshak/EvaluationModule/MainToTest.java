package com.Modelshak.EvaluationModule;

import org.json.JSONObject;

import java.io.File;

public class MainToTest {

    public static void main(String[] args) throws Exception {

       //String filename = "01a7f4f66453c739b99068603becb844-model.xml";
       String filename = "src/main/resources/common.uml";

       EvaluationModuleService evaluationModuleService = new EvaluationModuleService();

        JSONObject obj = new JSONObject();
        obj.put(filename, evaluationModuleService.evaluateModel(filename));
        obj.isEmpty();
    }


    }
