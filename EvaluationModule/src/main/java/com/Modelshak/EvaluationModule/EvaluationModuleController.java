package com.Modelshak.EvaluationModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluationModuleController {

    @Autowired
    private EvaluationModuleService evaluationModuleService;

    @GetMapping("/hello")
    public void hello(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
        evaluationModuleService.evaluate();
    }
}
