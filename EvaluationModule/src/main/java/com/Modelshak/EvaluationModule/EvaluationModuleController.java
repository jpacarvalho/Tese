package com.Modelshak.EvaluationModule;

import com.Modelshak.EvaluationModule.pojo.Project;
import com.Modelshak.EvaluationModule.pojo.Request;
import com.Modelshak.EvaluationModule.pojo.Response;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class EvaluationModuleController {

    @Autowired
    private EvaluationModuleService evaluationModuleService;

    private static final Logger logger = LoggerFactory.getLogger(EvaluationModuleController.class);

    @PostMapping(path = "/evaluateSingle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> evaluateSingle(HttpServletRequest request) throws Exception {
        ArrayList<String> list = Collections.list(request.getParameterNames());
        logger.info("parametros recebidos: {}", list.size() );

        list.forEach(p -> logger.info("parametro: {}", p));


        String jsonString = request.getParameter("data");

        logger.info("request: {}", jsonString);

        Gson gson = new Gson();
        Request requestObject = gson.fromJson(jsonString, Request.class);

        JSONObject obj = new JSONObject();
        String key = requestObject.getProjects().get(0).getKey();
        obj = obj.put(key, evaluationModuleService.evaluateModel(key));
        logger.info("response: {}", obj);

        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);

    }

    @PostMapping("/evaluateBatch")
    @ResponseBody
    public JSONObject evaluateBatch(HttpServletRequest request) throws Exception {

        String jsonString = request.getParameter("data");
        logger.info("request: {}", jsonString);
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
        logger.info("response: {}", obj);
        return obj;
    }

}
