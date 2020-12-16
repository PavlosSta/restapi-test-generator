package org.pavlos.restapispec.interfaces;

/**
 * This is a Method specification.
 */
public interface MethodSpec {

    enum MethodType {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE
    }

    /**
     * @return the type of the Method (GET, PUT, POST, PATCH, DELETE)
     */
    MethodType getType();

    /**
     * @return the Request Specification of the Method
     */
    RequestSpec getRequest();

    /**
     * @return the Response Specification of the Method
     */
    ResponseSpec getResponse();

}

