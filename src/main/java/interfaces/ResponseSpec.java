package interfaces;

import java.util.Set;

/**
 * This is a Response specification.
 */
public interface ResponseSpec {

    /**
     * @return a set with the Header Specifications of the Response
     */
    Set<HeaderSpec> getHeaders();

    /**
     * @return a set with the Status Specifications of the Request
     */
    Set<StatusSpec> getStatuses();

}

