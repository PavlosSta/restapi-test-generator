package org.pavlos.restapispec.interfaces;

public interface RequestURLSpec extends RequestSpec {

    default String getContentType() {
        return "application/x-www-form-urlencoded";
    }


}
