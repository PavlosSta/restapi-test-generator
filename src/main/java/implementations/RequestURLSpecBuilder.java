package implementations;

import interfaces.HeaderSpec;
import interfaces.ParameterSpec;
import interfaces.RequestSpec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RequestURLSpecBuilder {

    private final Set<HeaderSpec> headers = new LinkedHashSet<>();
    private final Set<ParameterSpec> queryParams = new LinkedHashSet<>();
    private final Set<ParameterSpec> bodyParams = new LinkedHashSet<>();

    public RequestURLSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestURLSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public RequestURLSpecBuilder addQueryParams(Set<ParameterSpec> queryParams) {
        this.queryParams.addAll(queryParams);
        return this;
    }

    public RequestURLSpecBuilder addQueryParam(ParameterSpec queryParam) {
        this.queryParams.add(queryParam);
        return this;
    }

    public RequestURLSpecBuilder addBodyParams(Set<ParameterSpec> bodyParams) {
        this.queryParams.addAll(bodyParams);
        return this;
    }

    public RequestURLSpecBuilder addBodyParam(ParameterSpec bodyParam) {
        this.queryParams.add(bodyParam);
        return this;
    }

    public RequestSpec build() {

        if (false) {
            throw new RuntimeException("Request Parameter bad input");
        }

        return new RequestURLSpecImpl(Collections.unmodifiableSet(headers), Collections.unmodifiableSet(queryParams), Collections.unmodifiableSet(bodyParams));

    }

}
