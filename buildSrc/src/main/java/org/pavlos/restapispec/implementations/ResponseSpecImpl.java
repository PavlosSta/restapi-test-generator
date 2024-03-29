package org.pavlos.restapispec.implementations;

import java.util.Map;
import java.util.Set;

import org.pavlos.restapispec.interfaces.HeaderSpec;
import org.pavlos.restapispec.interfaces.ResponseSpec;

public class ResponseSpecImpl implements ResponseSpec {

    private final Set<HeaderSpec> headers;
    private final String responseBodySchema;
    private final Integer code;
    private final Map<String, String> bodyAttributes;

    ResponseSpecImpl(Set<HeaderSpec> headers, String responseBodySchema, Integer code, Map<String, String> bodyAttributes) {
        this.headers = headers;
        this.responseBodySchema = responseBodySchema;
        this.code = code;
        this.bodyAttributes = bodyAttributes;
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

    public Map<String, String> getBodyAttributes() {
        return this.bodyAttributes;
    }


}