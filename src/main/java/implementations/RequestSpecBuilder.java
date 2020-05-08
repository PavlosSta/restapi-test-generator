package implementations;

import interfaces.HeaderSpec;
import interfaces.QueryParamSpec;
import interfaces.RequestSpec;

import java.util.LinkedHashSet;
import java.util.Set;

public class RequestSpecBuilder {

    private Set<HeaderSpec> headers = new LinkedHashSet<>();
    private Set<QueryParamSpec> queryParams = new LinkedHashSet<>();
    private String jwt;

    public RequestSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestSpecBuilder addHeader(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public RequestSpecBuilder addQueryParams(Set<QueryParamSpec> queryParams) {
        this.queryParams.addAll(queryParams);
        return this;
    }

    public RequestSpecBuilder addQueryParam(QueryParamSpec queryParam) {
        this.queryParams.add(queryParam);
        return this;
    }

    public RequestSpecBuilder setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public RequestSpec build() {

        // can headers or params be empty?
        if (headers.isEmpty() || queryParams.isEmpty()) {
            throw new RuntimeException();
        }
        else {
            return new RequestSpecImpl(headers, queryParams, jwt);
        }
    }

}
