package interfaces;

/**
 * This is a Query Parameter specification.
 */
public interface QueryParamSpec {

    /**
     * @return the name of the Query Parameter
     */
    String getName();

    /**
     * @return the type of the Query Parameter (integer, string etc)
     */
    String getType();

    /**
     * @return the value of the Query Parameter
     */
    String getValue();

    /**
     * @return the default value of the Query Parameter (if mandatory)
     */
    String getDefaultValue();

    /**
     * @return true if Query Parameter is mandatory
     */
    boolean isMandatory();

}

