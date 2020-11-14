package org.pavlos.restapispec.interfaces;

import java.util.Set;

/**
 * This is an API specification.
 */
public interface APISpec {

    /**
     * @return the base URL of the API
     */
    String getBaseUrl();

    /**
     * @return the label of the API
     */
    String getLabel();

    /**
     * @return a set with the Endpoint Specifications of the API
     */
    Set<EndpointSpec> getEndpoints();

}
