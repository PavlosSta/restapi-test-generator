package interfaces;

import java.util.Set;

public interface EndpointSpec {

    String getPath();
    String getLabel();
    Set<String> getDocs();
    Set<MethodSpec> getMethods();

}
