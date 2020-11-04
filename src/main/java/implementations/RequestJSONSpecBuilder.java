package implementations;

import interfaces.HeaderSpec;
import interfaces.ParameterSpec;
import interfaces.RequestJSONSpec;
import interfaces.RequestSpec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RequestJSONSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private final Set<ParameterSpec> queryParams = new LinkedHashSet<>();
    private String body;

    public RequestJSONSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestJSONSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public RequestJSONSpecBuilder addQueryParams(Set<ParameterSpec> queryParams) {
        this.queryParams.addAll(queryParams);
        return this;
    }

    public RequestJSONSpecBuilder addQueryParam(ParameterSpec queryParam) {
        this.queryParams.add(queryParam);
        return this;
    }

    public RequestJSONSpecBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public RequestJSONSpec build() {
        return new RequestJSONSpecImpl(Collections.unmodifiableSet(headers), Collections.unmodifiableSet(queryParams), this.body);
    }

}
