package implementations;

import interfaces.HeaderSpec;
import interfaces.ResponseSpec;
import interfaces.StatusSpec;

import java.util.LinkedHashSet;
import java.util.Set;

public class ResponseSpecBuilder {

    private Set<HeaderSpec> headers = new LinkedHashSet<>(); ;
    private Set<StatusSpec> statuses = new LinkedHashSet<>(); ;

    public ResponseSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public ResponseSpecBuilder addHeaders(HeaderSpec header) {
        this.headers.add(header);
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

        //TODO validate

        return new ResponseSpecImpl(headers, statuses);
    }

}
