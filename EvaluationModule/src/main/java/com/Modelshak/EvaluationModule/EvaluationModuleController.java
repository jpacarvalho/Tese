package com.Modelshak.EvaluationModule;

import com.Modelshak.EvaluationModule.pojo.Project;
import com.Modelshak.EvaluationModule.pojo.Request;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class EvaluationModuleController {

    @Autowired
    private EvaluationModuleService evaluationModuleService;

    @PostMapping("/evaluateSingle")
    @ResponseBody
    public JSONObject evaluateSingle(HttpServletRequest request) throws Exception {

        String jsonString = request.getParameter("data");
        Gson gson = new Gson();
        Request requestObject = gson.fromJson(jsonString, Request.class);

        JSONObject obj = new JSONObject();
        String key = requestObject.getProjects().get(0).getKey();
        obj.put(key, evaluationModuleService.evaluateModel(key));
        return obj;

    }

    @PostMapping("/evaluateBatch")
    @ResponseBody
    public JSONObject evaluateBatch(HttpServletRequest request) throws Exception {

        String jsonString = request.getParameter("data");
        Gson gson = new Gson();
        Request requestObject = gson.fromJson(jsonString, Request.class);

        JSONObject obj = new JSONObject();

        List<Project> fileList = requestObject.getProjects();

        fileList.forEach( fileModel ->
                {
                    try {
                        obj.put(fileModel.getKey(), evaluationModuleService.evaluateModel(fileModel.getKey()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        );

        return obj;
    }

}
