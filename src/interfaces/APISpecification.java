package interfaces;

import java.util.Set;

// this is an API specification
public interface APISpecification {

    String getBaseUrl();

    void setBaseUrl(String baseUrl);

    String getLabel();

    void setLabel(String label);

    Set<Endpoint> getEndpoints();

    void setEndpoints(Set<Endpoint> endpoints);

}
