package org.pavlos.restapispec.implementations;

import java.util.Set;

import org.pavlos.restapispec.interfaces.HeaderSpec;
import org.pavlos.restapispec.interfaces.ResponseSpec;
import org.pavlos.restapispec.interfaces.StatusSpec;

public class ResponseSpecImpl implements ResponseSpec {

    private final Set<HeaderSpec> headers;
    private final StatusSpec status;
    private final String responseBodySchema;

    ResponseSpecImpl(Set<HeaderSpec> headers, StatusSpec status, String responseBodySchema) {
        this.headers = headers;
        this.status = status;
        this.responseBodySchema = responseBodySchema;
    }

    public String getResponseBodySchema() {
        return this.responseBodySchema;
    }

    public Set<HeaderSpec> getHeaders() {
        return this.headers;
    }

    public StatusSpec getStatus() {
        return this.status;
    }

}