package com.Modelshak.EvaluationModule;

import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.evl.EvlModule;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EvaluationModuleService {

    public void evaluateEol() throws Exception {

        File file = new File("src/main/resources/count.eol");
        EolModule module = new EolModule();
        UmlModel model = new UmlModel();

        model.setModelFile("src/main/resources/prep.uml");
        model.load();

        module.getContext().getModelRepository().addModel(model);
        module.parse(file);
        module.execute();

    }

    public void evaluateEvl() throws Exception {

        File file = new File("src/main/resources/validate.evl");
        EolModule module = new EvlModule();
        UmlModel model = new UmlModel();

        model.setModelFile("src/main/resources/prep.uml");
        model.load();

        module.getContext().getModelRepository().addModel(model);
        module.parse(file);
        module.execute();

    }

}
