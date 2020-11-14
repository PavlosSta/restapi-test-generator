package org.pavlos.restapispec.interfaces;

/**
 * This is a Header specification.
 */
public interface HeaderSpec {

    /**
     * @return the name of the Header
     */
    String getName();

    /**
     * @return the default value of the Header (if mandatory)
     */
    String getDefaultValueIfOptionalAndMissing();

    /**
     * @return true if Header is mandatory
     */
    boolean isMandatory();
}

