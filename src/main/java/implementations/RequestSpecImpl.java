package implementations;

import java.util.Set;

import interfaces.HeaderSpec;
import interfaces.QueryParamSpec;
import interfaces.RequestSpec;

public class RequestSpecImpl implements RequestSpec {

    private final Set<HeaderSpec> headers;
    private final Set<QueryParamSpec> queryParams;
    private final String jwt;

    RequestSpecImpl(Set<HeaderSpec> headers, Set<QueryParamSpec> queryParams, String jwt) {
        this.headers = headers;
        this.queryParams = queryParams;
        this.jwt = jwt;
    }

    public Set<HeaderSpec> getHeaders() {
        return headers;
    }

    public Set<QueryParamSpec> getQueryParams() {
        return queryParams;
    }

    public String getJwt() {
        return jwt;
    }

}