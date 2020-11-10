package org.pavlos.interfaces;

import java.util.Set;

/**
 * This is a Request specification.
 */
public interface RequestSpec {

    /**
     * @return a set with the Header Specifications of the Request
     */
    Set<HeaderSpec> getHeaders();

    /**
     * @return a set with the Query Parameter Specifications of the Request
     */
    Set<ParameterSpec> getQueryParams();

    /**
     * @return the Content Type header of the Request
     */
    String getContentType();

}

/*


*/