package interfaces;

import java.util.Set;

// this is an API specification
public interface APISpec {

    //TODO javadoc

    //TODO java template engines!

    String getBaseUrl();
    String getLabel();
    Set<EndpointSpec> getEndpoints();

}
