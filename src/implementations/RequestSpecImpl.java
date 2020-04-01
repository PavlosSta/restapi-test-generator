package implementations;

import java.util.Set;

import interfaces.Header;
import interfaces.QueryParam;
import interfaces.RequestSpec;

public class RequestSpecImpl implements RequestSpec {

    Set<Header> headers;
    Set<QueryParam> queryParams;
    String jwt;


    public Set<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<Header> headers) {
        this.headers = headers;
    }

    public Set<QueryParam> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Set<QueryParam> queryParams) {
        this.queryParams = queryParams;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}