package interfaces;

import java.util.Set;

public interface Endpoint {

    String getPath();    // eg. "prices"
    String getLabel();   // eg. "endpoint for prices"

    Set<String> getDocs();    // notes for using this endpoint

    Set<Method> getMethods(); // endpoint 1-N methods

}
