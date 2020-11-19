package org.pavlos.restapispec.interfaces;

/**
 * This is a Parameter specification.
 */
public interface ParameterSpec {

    /**
     * @return the name of the Parameter
     */
    String getName();

    /**
     * @return the type of the Parameter (String, Integer, Float, Boolean)
     */
    String getType();

    /**
     * @return the default body of the Parameter (if mandatory)
     */
    String getDefaultBodyIfOptionalAndMissing();

    /**
     * @return true if Parameter is mandatory
     */
    boolean getMandatory();

}

