package implementations;

import interfaces.HeaderSpec;
import interfaces.ParameterSpec;
import interfaces.RequestSpec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RequestJSONSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private final Set<ParameterSpec> queryParams = new LinkedHashSet<>();
    private final Set<ParameterSpec> bodyParams = new LinkedHashSet<>();

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

    public RequestSpec build() {

        if (false) {
            throw new RuntimeException("Request Parameter bad input");
        }

        return new RequestJSONSpecImpl(Collections.unmodifiableSet(headers), Collections.unmodifiableSet(queryParams), Collections.unmodifiableSet(bodyParams));

    }

}

