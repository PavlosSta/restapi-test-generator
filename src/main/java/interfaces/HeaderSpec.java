package interfaces;

/**
 * This is a Header specification.
 */
public interface HeaderSpec {

    /**
     * @return the name of the Header
     */
    String getName();

    /**
     * @return the value of the Header
     */
    String getValue();

    /**
     * @return the default value of the Header (if mandatory)
     */
    String defaultValueIfOptionalAndMissing();

    /**
     * @return true if Header is mandatory
     */
    boolean isMandatory();
}

