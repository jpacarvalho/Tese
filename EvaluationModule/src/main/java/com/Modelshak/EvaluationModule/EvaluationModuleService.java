package com.Modelshak.EvaluationModule;

import com.Modelshak.EvaluationModule.constraints.FileConstraint;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.util.UMLValidator;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        System.out.println("");

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

        public void test(@FileConstraint File file) throws IOException {



            ResourceSet resourceSet = new ResourceSetImpl();
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
            resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
            resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(resourceSet.getResourceFactoryRegistry().DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
            Map<URI,URI> uriMap = resourceSet.getURIConverter().getURIMap();


            final URI uri2 = URI.createURI("jar:file:D:/.../plugins/org.eclipse.uml2.uml.resources_4.1.0.v20130506-1015.jar!/");

            URI uri = URI.createFileURI("src/main/resources/prep.uml");
            uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri2.appendSegment("libraries").appendSegment(""));
            uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri2.appendSegment("metamodels").appendSegment(""));
            uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri2.appendSegment("profiles").appendSegment(""));
            uriMap.put(URI.createURI("pathmap://Papyrus.profile.uml"),URI.createURI("file:/D:/.../Papyrus.profile.uml/"));

            EPackage.Registry.INSTANCE.put("src/main/resources/prep.uml",UMLPackage.eINSTANCE);
            Resource resource = resourceSet.createResource(uri);
            try {
                resource.load(null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Model m = (Model) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.MODEL);

            UMLValidator umlValidator = new UMLValidator();
            umlValidator.validateModel(m, null, null);


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
