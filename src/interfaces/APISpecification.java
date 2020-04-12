package interfaces;

import java.util.Set;

// this is an API specification
public interface APISpecification {

    String getBaseUrl();
    String getLabel();
    Set<Endpoint> getEndpoints();

}
