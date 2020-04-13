package implementations;

import interfaces.HeaderSpec;
import interfaces.ResponseSpec;
import interfaces.StatusSpec;

import java.util.Set;

public class ResponseSpecBuilder {

    private Set<HeaderSpec> headers;
    private Set<StatusSpec> statuses;

    public ResponseSpecBuilder headers(Set<HeaderSpec> headers) {
        this.headers = headers;
        return this;
    }

    public ResponseSpecBuilder statuses(Set<StatusSpec> statuses) {
        this.statuses = statuses;
        return this;
    }

    public ResponseSpec build() {
        return new ResponseSpecImpl(headers, statuses);
    }

}
