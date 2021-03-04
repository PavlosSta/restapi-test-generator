package org.pavlos.restapispec.implementations;

import org.pavlos.restapispec.interfaces.HeaderSpec;
import org.pavlos.restapispec.interfaces.ResponseSpec;

import java.util.*;

public class ResponseSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private String responseBody;
    private Integer code;
    private final Map<String, String> bodyAttributes = new HashMap<>();


    public ResponseSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public ResponseSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public ResponseSpecBuilder addResponseBodySchema(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public ResponseSpecBuilder setCode(Integer code) {
        this.code = code;
        return this;
    }

    public ResponseSpecBuilder addBodyAttribute(String bodyAttributeName, String bodyAttributeType) {
        this.bodyAttributes.put(bodyAttributeName, bodyAttributeType);
        return this;
    }

    public ResponseSpec build() {

        if (code == null) {
            throw new RuntimeException("Response: Status missing");
        }
        if (code < 200 || code > 299) {
            throw new RuntimeException("Response: Status not supported (give positive code 200-299)");
        }
        if (!(responseBody.equals("JSON") || responseBody.equals("Text"))) {
            throw new RuntimeException("Response: Response body schema not supported (give JSON or Text)");
        }
        if (responseBody.equals("Text") && !(bodyAttributes.isEmpty())) {
            throw new RuntimeException("Response: Body attributes supported only for JSON");
        }
        return new ResponseSpecImpl(Collections.unmodifiableSet(headers), responseBody, code, Collections.unmodifiableMap(bodyAttributes));

    }

}
