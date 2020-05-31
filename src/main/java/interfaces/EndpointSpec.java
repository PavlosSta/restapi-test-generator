package interfaces;

import java.util.Set;

/**
 * This is an Endpoint specification.
 */
public interface EndpointSpec {

    /**
     * @return the path of the Endpoint
     */
    String getPath();

    /**
     * @return the label of the Endpoint
     */
    String getLabel();

    /**
     * @return the description of the Endpoint
     */
    String getDescription();

    /**
     *
     * @return the attribute of the Endpoint if it exists, eg. {id} from products/{id}
     */
    String getAttribute();

    /**
     * @return a set with the Method Specifications of the Endpoint
     */
    Set<MethodSpec> getMethods();

}
