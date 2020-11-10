package org.pavlos.implementations;

import org.pavlos.interfaces.HeaderSpec;
import org.pavlos.interfaces.ResponseSpec;
import org.pavlos.interfaces.StatusSpec;

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
            throw new RuntimeException("Response: Status missing");
        }
        if (!(status.getCode().equals("200") || status.getCode().equals("201"))) {
            throw new RuntimeException("Response: Status not supported (give 200 or 201)");
        }
        if (!(responseBody.equals("JSON") || responseBody.equals("String") || responseBody.equals("Integer") || responseBody.equals("int"))) {
            throw new RuntimeException("Response: Response body schema not supported (give JSON, String or Integer)");
        }
        return new ResponseSpecImpl(Collections.unmodifiableSet(headers), status, responseBody);

    }

}
