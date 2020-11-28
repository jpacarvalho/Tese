package com.Modelshak.EvaluationModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class EvaluationModuleController {

    @Autowired
    private EvaluationModuleService evaluationModuleService;

    @GetMapping("/hello")
    public void hello() throws Exception {
        File file = new File("src/main/resources/validate.evl");
        evaluationModuleService.evaluateEol(file);
        evaluationModuleService.evaluateEvl(file);
    }

}
