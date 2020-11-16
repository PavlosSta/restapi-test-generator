package org.pavlos.restapispec.interfaces;

public interface RequestJSONSpec extends RequestSpec {

    default String getContentType() {
        return "application/json";
    }

    /**
     * @return the body schema of the Request (JSON, String, Integer)
     */
    String getRequestBodySchema();

}