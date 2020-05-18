package implementations;

import java.util.Set;

import interfaces.HeaderSpec;
import interfaces.QueryParamSpec;
import interfaces.RequestSpec;

public class RequestSpecImpl implements RequestSpec {

    private final Set<HeaderSpec> headers;
    private final Set<QueryParamSpec> queryParams;

    RequestSpecImpl(Set<HeaderSpec> headers, Set<QueryParamSpec> queryParams) {
        this.headers = headers;
        this.queryParams = queryParams;
    }

    public Set<HeaderSpec> getHeaders() {
        return this.headers;
    }

    public Set<QueryParamSpec> getQueryParams() {
        return this.queryParams;
    }

}