package interfaces;

import java.util.Set;

public interface RequestJSONSpec extends RequestSpec {

    default String getContentType() {
        return "application/json";
    }

    /**
     * @return a set with the Body Parameter Specifications of the Request
     */
    String getBody();

}
