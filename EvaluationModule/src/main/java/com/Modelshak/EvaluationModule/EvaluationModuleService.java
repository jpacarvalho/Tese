package com.Modelshak.EvaluationModule;

import com.Modelshak.EvaluationModule.enumerations.EolEnum;
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

    public JSONObject evaluateModel(String filename) throws Exception {

        JSONObject obj = new JSONObject();

        getFileFrombucket(filename);
        List<Metric> metrics = evaluateEol(filename);
        List<ValidationResult> validations = evaluateEvl(filename);
        List<Validation> errors = new ArrayList<>();
        List<Validation> warnings = new ArrayList<>();

        for (ValidationResult validation : validations) {
            errors.addAll(validation.getErrors());
            warnings.addAll(validation.getWarnings());
        }

        obj.put("metrics", metrics);
        obj.put("errors", errors);
        obj.put("warnings", warnings);

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

        for (EolEnum eolfile : EolEnum.values()) {
            ValidationResult singleResult = evaluateSingleEvl(filename, eolfile.getPath());
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

        String path = "auxFile.txt";
        List<Metric> metrics = new ArrayList<>();
        Path p = Paths.get(path);
        String line = "";

        try {
            BufferedReader reader = Files.newBufferedReader(p);
            line = reader.readLine();
        } catch (IOException ignored) {

        }

        String[] splited = line.split(",");

        for(int i = 0; i < splited.length; i++){
            String[] result = splited[i].split("-");

            metrics.add(new Metric(result[0], result[1]));
        }

        File file = new File(path);
        file.delete();

        return metrics;
    }

    private void getFileFrombucket(String key) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        String bucketName = "myawsbucketupload";

        System.out.format("Downloading %s from S3 bucket %s...\n", key, bucketName);

        try {
            S3Object o = s3.getObject(bucketName, key);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(key);
            byte[] read_buf = new byte[1024];
            int read_len;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);

        }
    }

}
