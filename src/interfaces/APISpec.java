package interfaces;

import java.util.Set;

// this is an API specification
public interface APISpec {

    String getBaseUrl();
    String getLabel();
    Set<EndpointSpec> getEndpoints();

}
