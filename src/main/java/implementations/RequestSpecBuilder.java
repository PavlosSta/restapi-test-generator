package implementations;

import interfaces.HeaderSpec;
import interfaces.QueryParamSpec;
import interfaces.RequestSpec;

import java.util.Set;

public class RequestSpecBuilder {

    private Set<HeaderSpec> headers;
    private Set<QueryParamSpec> queryParams;
    private String jwt;

    public RequestSpecBuilder setHeaders(Set<HeaderSpec> headers) {
        this.headers = headers;
        return this;
    }

    public RequestSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestSpecBuilder addHeaders(HeaderSpec header) {
        this.headers.add(header);
        return this;
    }

    public RequestSpecBuilder setQueryParams(Set<QueryParamSpec> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public RequestSpecBuilder addQueryParams(Set<QueryParamSpec> queryParams) {
        this.queryParams.addAll(queryParams);
        return this;
    }

    public RequestSpecBuilder addQueryParams(QueryParamSpec queryParam) {
        this.queryParams.add(queryParam);
        return this;
    }

    public RequestSpecBuilder setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public RequestSpec build() {
        return new RequestSpecImpl(headers, queryParams, jwt);
    }

}
