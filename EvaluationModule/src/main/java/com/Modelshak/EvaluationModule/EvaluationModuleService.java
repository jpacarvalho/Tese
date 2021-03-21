package com.Modelshak.EvaluationModule;

import com.Modelshak.EvaluationModule.enumerations.EolEnum;
import com.Modelshak.EvaluationModule.enumerations.EvlEnum;
import com.Modelshak.EvaluationModule.pojo.Metric;
import com.Modelshak.EvaluationModule.pojo.Validation;
import com.Modelshak.EvaluationModule.pojo.ValidationResult;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Validated
public class EvaluationModuleService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationModuleService.class);

    public JSONObject evaluateModel(String filename) throws Exception {

        JSONObject obj = new JSONObject();

        getFileFrombucket(filename);

        List<Metric> metrics = new ArrayList<>();
        List<ValidationResult> validations = new ArrayList<>();

        try {
            metrics = evaluateEol(filename);
        } catch (Exception e) {
            logger.error("Nao foi possivel efetuar o calculo de metricas para o modelo {}", filename);
        }


        try {
            validations = evaluateEvl(filename);
        } catch (Exception e) {
            logger.error("Nao foi possivel efetuar o calculo de metricas para o modelo {}", filename);
        }

        List<Validation> errors = new ArrayList<>();
        List<Validation> warnings = new ArrayList<>();

        for (ValidationResult validation : validations) {
            errors.addAll(validation.getErrors());
            warnings.addAll(validation.getWarnings());
        }

        obj.put("metrics", metrics);
        obj.put("errors", errors);
        obj.put("warnings", warnings);

        logger.info("Número de métricas no modelo {}: {}", filename, metrics.size());
        logger.info("Número de errors no modelo {}: {}",filename, errors.size());
        logger.info("Número de warnings no modelo {}: {}", filename, warnings.size());

        File file = new File(filename);
        file.delete();

        return obj;
    }
    private List<Metric> evaluateEol(String filename) throws Exception {

        List<Metric> metrics = new ArrayList<>();

        for (EolEnum eolfile : EolEnum.values()) {
            List<Metric> singleResult = evaluateSingleEol(filename, eolfile.getPath());
            metrics.addAll(singleResult);
        }

        return metrics;
    }

    private List<ValidationResult> evaluateEvl(String filename) throws Exception {
        List<ValidationResult> validations = new ArrayList<>();

        for (EvlEnum evlfile : EvlEnum.values()) {
            ValidationResult singleResult = evaluateSingleEvl(filename, evlfile.getPath());
            validations.add(singleResult);
        }

        return validations;
    }

    private List<Metric> evaluateSingleEol(String filename, String validationsFilename) throws Exception {

        EolModule module = new EolModule();
        UmlModel model = new UmlModel();

        model.setModelFile(filename);
        model.load();

        module.getContext().getModelRepository().addModel(model);
        File fileEol = new File(validationsFilename);
        module.parse(fileEol);
        module.execute();

        return getMetrics();

    }

    private ValidationResult evaluateSingleEvl(String filename, String validationsFilename) throws Exception {

        ValidationResult validationResult = new ValidationResult();
        EvlModule module = new EvlModule();
        UmlModel model = new UmlModel();

        model.setModelFile(filename);
        model.load();

        module.getContext().getModelRepository().addModel(model);
        File fileEvl = new File(validationsFilename);
        module.parse(fileEvl);
        module.execute();
        Collection<UnsatisfiedConstraint> unsatisfiedList = module.getContext().getUnsatisfiedConstraints();

        for (UnsatisfiedConstraint constraint : unsatisfiedList) {

            if (constraint.getConstraint().isCritique()){
                validationResult.getWarnings().add(
                        new Validation(null, false, constraint.getMessage()));
            }else{
                validationResult.getErrors().add(
                        new Validation(null, false, constraint.getMessage()));
            }
        }

        return validationResult;
    }

    private List<Metric> getMetrics() {

        String path = "src/main/resources/auxFile.txt";
        List<Metric> metrics = new ArrayList<>();
        Path p = Paths.get(path);
        String line = "";

        try {
            BufferedReader reader = Files.newBufferedReader(p);
            line = reader.readLine();
            logger.info("{}", line);

            if(line != null && !line.isEmpty()){
                String[] splited = line.split(",");


                for(int i = 0; i < splited.length; i++){
                    String[] result = splited[i].split("-");

                    metrics.add(new Metric(result[0], result[1]));
                }
            }

        } catch (IOException ignored) {
            logger.error("Falha ao ler linha ficheiro auxiliar");
        }



        File file = new File(path);
        logger.info("aux: {}", file.length());
        file.delete();

        return metrics;
    }

    private void getFileFrombucket(String key) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        String bucketName = "myawsbucketupload";

        logger.info("Downloading {} from bucket {}", key, bucketName);

        try {
            S3Object o = s3.getObject(bucketName, key);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(key);
            byte[] readBuf = new byte[1024];
            int readLen;
            while ((readLen = s3is.read(readBuf)) > 0) {
                fos.write(readBuf, 0, readLen);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
