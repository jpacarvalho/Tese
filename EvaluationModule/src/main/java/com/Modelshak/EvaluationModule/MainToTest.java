package com.Modelshak.EvaluationModule;

import java.io.File;

public class MainToTest {

    public static void main(String[] args) throws Exception {

       String filename = "01a7f4f66453c739b99068603becb844-model.xml";

       EvaluationModuleService evaluationModuleService = new EvaluationModuleService();

       evaluationModuleService.evaluateModel(filename);

    }


    }
