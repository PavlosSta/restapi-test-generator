package interfaces;

import java.util.Set;

public interface Endpoint {

    String getPath();
    String getLabel();
    Set<String> getDocs();
    Set<Method> getMethods();

}
