package com.Modelshak.EvaluationModule;

import com.Modelshak.EvaluationModule.constraints.FileConstraint;
import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Validated
public class EvaluationModuleService {

    public static final String WARNING_MESSAGE = "[WARNING] ";
    public static final String ERROR_MESSAGE = "[ERROR] ";

    public void evaluateEol(@FileConstraint File file) throws Exception {

        EolModule module = new EolModule();
        UmlModel model = new UmlModel();

        model.setModelFile("src/main/resources/prep.uml");
        model.load();

        module.getContext().getModelRepository().addModel(model);
        module.parse(file);
        module.execute();

    }

    public void evaluateEvl(@FileConstraint File file) throws Exception {

        EvlModule module = new EvlModule();
        UmlModel model = new UmlModel();

        model.setModelFile("src/main/resources/prep.uml");
        model.load();

        module.getContext().getModelRepository().addModel(model);
        module.parse(file);
        module.execute();
        Collection<UnsatisfiedConstraint> unsatisfiedList = module.getContext().getUnsatisfiedConstraints();

        for (UnsatisfiedConstraint constraint : unsatisfiedList) {

            if (constraint.getConstraint().isCritique()){
                System.out.println(WARNING_MESSAGE + constraint.getMessage());
            }else{
                System.out.println(ERROR_MESSAGE +constraint.getMessage());
            }
        }

        }

        @ExceptionHandler({ConstraintViolationException.class})
        public void FileExceptionHandler(ConstraintViolationException ex){
            List<String> errors = new ArrayList<String>();
            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                errors.add(violation.getRootBeanClass().getName() + " " +
                        violation.getPropertyPath() + ": " + violation.getMessage());
            }
        }

}
