package implementations;

import java.util.Set;

import interfaces.HeaderSpec;
import interfaces.ResponseSpec;
import interfaces.StatusSpec;

public class ResponseSpecImpl implements ResponseSpec {

    private final Set<HeaderSpec> headers;
    private final Set<StatusSpec> statuses;

    ResponseSpecImpl(Set<HeaderSpec> headers, Set<StatusSpec> statuses) {
        this.headers = headers;
        this.statuses = statuses;
    }

    public Set<HeaderSpec> getHeaders() {
        return headers;
    }

    public Set<StatusSpec> getStatuses() {
        return statuses;
    }

}