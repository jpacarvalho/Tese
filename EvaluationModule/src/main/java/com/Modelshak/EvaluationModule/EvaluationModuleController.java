package com.Modelshak.EvaluationModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluationModuleController {

    @Autowired
    private EvaluationModuleService evaluationModuleService;

    @GetMapping("/hello")
    public void hello() throws Exception {
        evaluationModuleService.evaluateEol();
        evaluationModuleService.evaluateEvl();
    }
}
