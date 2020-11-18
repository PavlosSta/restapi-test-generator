package org.pavlos.restapispec.interfaces;

public interface RequestJSONSpec extends RequestSpec {

    default String getContentType() {
        return "application/json";
    }

}
