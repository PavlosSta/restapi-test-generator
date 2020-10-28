package interfaces;

import java.util.Set;

/**
 * This is a Response specification.
 */
public interface ResponseSpec {

    /**
     * @return the body schema of the Response (JSON, String, Integer)
     */
    String getResponseBodySchema();

    /**
     * @return a set with the Header Specifications of the Response
     */
    Set<HeaderSpec> getHeaders();

    /**
     * @return a set with the Status Specifications of the Request
     */
    StatusSpec getStatus();

}

