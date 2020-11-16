package org.pavlos.restapispec.interfaces;

/**
 * This is a Status specification.
 */
public interface StatusSpec {

    /**
     * @return the code of the status
     */
    String getCode();

    /**
     * @return the body of the status
     */
    String getBody();

}
