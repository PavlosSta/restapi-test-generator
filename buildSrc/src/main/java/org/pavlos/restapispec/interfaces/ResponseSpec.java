package org.pavlos.restapispec.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * This is a Response specification.
 */
public interface ResponseSpec {

    /**
     * @return the body schema of the Response (JSON, Text)
     */
    String getResponseBodySchema();

    /**
     * @return the code of the status
     */
    Integer getCode();

    /**
     * @return a set with the Header Specifications of the Response
     */
    Set<HeaderSpec> getHeaders();

    /**
     * @return a map with the Body Attributes of the Response (Supported types: String, Number, JSON, Array, Boolean, null)
     */
    Map<String, String> getBodyAttributes();

}

