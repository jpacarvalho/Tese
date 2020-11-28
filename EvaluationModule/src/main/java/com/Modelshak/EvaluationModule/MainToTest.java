package com.Modelshak.EvaluationModule;

import java.io.File;

public class MainToTest {

    public static void main(String[] args) throws Exception {

        File file = new File("src/main/resources/validate.evl");

        EvaluationModuleService evaluationModuleService = new EvaluationModuleService();

        evaluationModuleService.evaluateEol(file);
        evaluationModuleService.evaluateEvl(file);
    }


    }
