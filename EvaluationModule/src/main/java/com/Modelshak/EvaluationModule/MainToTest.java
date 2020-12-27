package com.Modelshak.EvaluationModule;

import java.io.File;

public class MainToTest {

    public static void main(String[] args) throws Exception {

        File fileEol = new File("src/main/resources/countClass.eol");

        File file = new File("src/main/resources/validate.evl");

        EvaluationModuleService evaluationModuleService = new EvaluationModuleService();

        evaluationModuleService.evaluateEol(fileEol);
        evaluationModuleService.evaluateEvl(file);
        evaluationModuleService.test(file);
    }


    }
