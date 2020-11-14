package org.pavlos.restapispec.interfaces;

import java.util.Set;

public interface RequestURLSpec extends RequestSpec {

    default String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    /**
     * @return a set with the Body Parameter Specifications of the Request
     */
    Set<ParameterSpec> getBodyParams();
}
