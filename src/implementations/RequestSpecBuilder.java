package implementations;

import interfaces.HeaderSpec;
import interfaces.QueryParamSpec;
import interfaces.RequestSpec;

import java.util.Set;

public class RequestSpecBuilder {

    private Set<HeaderSpec> headers;
    private Set<QueryParamSpec> queryParams;
    private String jwt;

    public RequestSpecBuilder headers(Set<HeaderSpec> headers) {
        this.headers = headers;
        return this;
    }

    public RequestSpecBuilder queryParams(Set<QueryParamSpec> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public RequestSpecBuilder jwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public RequestSpec build() {
        return new RequestSpecImpl(headers, queryParams, jwt);
    }

}
