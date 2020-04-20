package implementations;

import interfaces.HeaderSpec;
import interfaces.ResponseSpec;
import interfaces.StatusSpec;

import java.util.Set;

public class ResponseSpecBuilder {

    private Set<HeaderSpec> headers;
    private Set<StatusSpec> statuses;

    public ResponseSpecBuilder setHeaders(Set<HeaderSpec> headers) {
        this.headers = headers;
        return this;
    }

    public ResponseSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public ResponseSpecBuilder addHeaders(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public ResponseSpecBuilder setStatuses(Set<StatusSpec> statuses) {
        this.statuses = statuses;
        return this;
    }

    public ResponseSpecBuilder addStatuses(Set<StatusSpec> statuses) {
        this.statuses.addAll(statuses);
        return this;
    }

    public ResponseSpecBuilder addStatuses(StatusSpec status) {
        this.statuses.add(status);
        return this;
    }

    public ResponseSpec build() {
        return new ResponseSpecImpl(headers, statuses);
    }

}
