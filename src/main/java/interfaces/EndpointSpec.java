package interfaces;

import java.util.Set;

public interface EndpointSpec {

    String getPath();
    String getLabel();
    String getDescription();
    Set<MethodSpec> getMethods();

}
