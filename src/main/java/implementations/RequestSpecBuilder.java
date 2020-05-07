package implementations;

import interfaces.HeaderSpec;
import interfaces.QueryParamSpec;
import interfaces.RequestSpec;

import java.util.LinkedHashSet;
import java.util.Set;

public class RequestSpecBuilder {

    private Set<HeaderSpec> headers = new LinkedHashSet<>(); ;
    private Set<QueryParamSpec> queryParams = new LinkedHashSet<>(); ;
    private String jwt;

    public RequestSpecBuilder addHeaders(Set<HeaderSpec> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public RequestSpecBuilder addHeaders(HeaderSpec header) {
        this.headers.add(header);
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

        //TODO validate

        return new RequestSpecImpl(headers, queryParams, jwt);
    }

}
