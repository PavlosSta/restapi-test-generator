package interfaces;

/**
 * This is a Method specification.
 */
public interface MethodSpec {

    /**
     * @return the type of the Method (GET, PUT, POST, PATCH, DELETE)
     */
    String getType();

    /**
     * @return the Request Specification of the Method
     */
    RequestSpec getRequest();

    /**
     * @return the Response Specification of the Method
     */
    ResponseSpec getResponse();

}

