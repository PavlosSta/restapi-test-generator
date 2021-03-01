package org.pavlos.restapispec.implementations;

import java.util.Set;

import org.pavlos.restapispec.interfaces.HeaderSpec;
import org.pavlos.restapispec.interfaces.ResponseSpec;

public class ResponseSpecImpl implements ResponseSpec {

    private final Set<HeaderSpec> headers;
    private final String responseBodySchema;
    private final Integer code;

    ResponseSpecImpl(Set<HeaderSpec> headers, String responseBodySchema, Integer code) {
        this.headers = headers;
        this.responseBodySchema = responseBodySchema;
        this.code = code;
    }

    public String getResponseBodySchema() {
        return this.responseBodySchema;
    }

    public Set<HeaderSpec> getHeaders() {
        return this.headers;
    }

    public Integer getCode() {
        return this.code;
    }

}