package com.Modelshak.EvaluationModule;

import java.io.File;

public class MainToTest {

    public static void main(String[] args) throws Exception {

       String filename = "src/main/resources/common75.uml";

       EvaluationModuleService evaluationModuleService = new EvaluationModuleService();

       evaluationModuleService.evaluateModel(filename);

    }


    }
