package interfaces;

import java.util.Set;

public interface ResponseSpec {

    Set<HeaderSpec> getHeaders();
    Set<StatusSpec> getStatuses();

}

