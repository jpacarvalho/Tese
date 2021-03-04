package com.Modelshak.EvaluationModule;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EvaluationModuleController {

    @Autowired
    private EvaluationModuleService evaluationModuleService;

    @GetMapping("/evaluateSingle")
    public JSONObject evaluateSingle(@RequestParam String fileModel) throws Exception {

        JSONObject obj = new JSONObject();
        obj.put(fileModel, evaluationModuleService.evaluateModel(fileModel));
        return obj;

    }

    @GetMapping("/evaluateBatch")
    public JSONObject evaluateBatch(@RequestParam List<String> fileList) throws Exception {

        JSONObject obj = new JSONObject();

        fileList.forEach( fileModel ->
                {
                    try {
                        obj.put(fileModel, evaluationModuleService.evaluateModel(fileModel));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        );

        return obj;
    }

}
