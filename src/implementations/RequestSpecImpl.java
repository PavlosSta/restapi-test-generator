package implementations;

import java.util.Set;

import interfaces.Header;
import interfaces.QueryParam;
import interfaces.RequestSpec;

public class RequestSpecImpl implements RequestSpec {

    Set<Header> headers;
    Set<QueryParam> queryParams;
    String jwt;

    public RequestSpecImpl(Set<Header> headers, Set<QueryParam> queryParams, String jwt) {
        this.headers = headers;
        this.queryParams = queryParams;
        this.jwt = jwt;
    }

    public Set<Header> getHeaders() {
        return headers;
    }

    public Set<QueryParam> getQueryParams() {
        return queryParams;
    }

    public String getJwt() {
        return jwt;
    }

}