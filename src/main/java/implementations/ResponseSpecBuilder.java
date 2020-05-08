package implementations;

import interfaces.HeaderSpec;
import interfaces.ResponseSpec;
import interfaces.StatusSpec;

import java.util.LinkedHashSet;
import java.util.Set;

public class ResponseSpecBuilder {

    private Set<HeaderSpec> headers = new LinkedHashSet<>();
    private Set<StatusSpec> statuses = new LinkedHashSet<>();

    public ResponseSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public ResponseSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public ResponseSpecBuilder addStatuses(Set<StatusSpec> statuses) {
        this.statuses.addAll(statuses);
        return this;
    }

    public ResponseSpecBuilder addStatus(StatusSpec status) {
        this.statuses.add(status);
        return this;
    }

    public ResponseSpec build() {

        if (headers.isEmpty() || statuses.isEmpty()) {
            throw new RuntimeException();
        }
        else {
            return new ResponseSpecImpl(headers, statuses);
        }
    }

}
