package implementations;

import interfaces.HeaderSpec;
import interfaces.ParameterSpec;
import interfaces.RequestSpec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RequestSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private final Set<ParameterSpec> queryParams = new LinkedHashSet<>();
    private final Set<ParameterSpec> bodyParams = new LinkedHashSet<>();
    private String body;
    private String contentType;

    public RequestSpecBuilder (String contentType) {
        this.contentType = contentType;
    }

    public RequestSpecBuilder() {

    }

    public RequestSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public RequestSpecBuilder addQueryParams(Set<ParameterSpec> queryParams) {
        this.queryParams.addAll(queryParams);
        return this;
    }

    public RequestSpecBuilder addQueryParam(ParameterSpec queryParam) {
        this.queryParams.add(queryParam);
        return this;
    }

    public RequestSpecBuilder addBodyParams(Set<ParameterSpec> bodyParams) {
        this.bodyParams.addAll(bodyParams);
        return this;
    }

    public RequestSpecBuilder addBodyParam(ParameterSpec bodyParam) {
        this.bodyParams.add(bodyParam);
        return this;
    }

    public RequestSpecBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public RequestSpec build() {

        if (!(contentType.equals("JSON") || contentType.equals("URL"))) {
            throw new RuntimeException("Request: Bad content type. Supported: JSON, URL");
        }
        if (headers.isEmpty()) {
            throw new RuntimeException("Request: Empty headers");
        }
        if (contentType.equals("JSON")) {
            return new RequestJSONSpecImpl(Collections.unmodifiableSet(headers), Collections.unmodifiableSet(queryParams), this.body);
        }
        else {
            return new RequestURLSpecImpl(Collections.unmodifiableSet(headers), Collections.unmodifiableSet(queryParams), Collections.unmodifiableSet(bodyParams));
        }

    }

}
