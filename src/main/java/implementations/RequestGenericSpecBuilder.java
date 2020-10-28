package implementations;

import interfaces.HeaderSpec;
import interfaces.ParameterSpec;
import interfaces.RequestSpec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RequestGenericSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private final Set<ParameterSpec> queryParams = new LinkedHashSet<>();
    private final Set<ParameterSpec> bodyParams = new LinkedHashSet<>();
    private String contentType;

    public RequestGenericSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestGenericSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public RequestGenericSpecBuilder addQueryParams(Set<ParameterSpec> queryParams) {
        this.queryParams.addAll(queryParams);
        return this;
    }

    public RequestGenericSpecBuilder addQueryParam(ParameterSpec queryParam) {
        this.queryParams.add(queryParam);
        return this;
    }

    public RequestGenericSpecBuilder addBodyParams(Set<ParameterSpec> bodyParams) {
        this.queryParams.addAll(bodyParams);
        return this;
    }

    public RequestGenericSpecBuilder addBodyParam(ParameterSpec bodyParam) {
        this.queryParams.add(bodyParam);
        return this;
    }

    public RequestGenericSpecBuilder setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public RequestSpec build() {

        if (headers.isEmpty() && queryParams.isEmpty() && bodyParams.isEmpty()) {
            throw new RuntimeException("Request Parameter bad input");
        }

        return new RequestGenericSpecImpl(Collections.unmodifiableSet(headers), Collections.unmodifiableSet(queryParams), Collections.unmodifiableSet(bodyParams), contentType);

    }

}
