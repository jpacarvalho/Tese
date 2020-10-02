import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.EolModule;

import java.io.File;

public class TestEpsilon {

    public static void main(String[] args) throws Exception {

        File file = new File("src/main/resources/count.eol");
        EolModule module = new EolModule();
        UmlModel model = new UmlModel();

        model.setModelFile("src/main/resources/prep.uml");
        model.load();

        module.getContext().getModelRepository().addModel(model);
        module.parse(file);
        module.execute();

    }

}
