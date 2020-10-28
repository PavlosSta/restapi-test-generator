package implementations;

import interfaces.HeaderSpec;
import interfaces.ResponseSpec;
import interfaces.StatusSpec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ResponseSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private StatusSpec status;
    private String responseBody;

    public ResponseSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public ResponseSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public ResponseSpecBuilder addStatus(StatusSpec status) {
        this.status = status;
        return this;
    }

    public ResponseSpecBuilder addResponseBodySchema(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public ResponseSpec build() {

        if (status == null) {
            throw new RuntimeException("Response bad input");
        }

        return new ResponseSpecImpl(Collections.unmodifiableSet(headers), status, responseBody);

    }

}
