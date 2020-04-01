package interfaces;

import java.util.Set;

public interface RequestSpec {

    Set<Header> getHeaders();

    void setHeaders(Set<Header> headers);

    Set<QueryParam>  getQueryParams();

    void setQueryParams(Set<QueryParam> queryParams);

    String getJwt();

    void setJwt(String jwt);

}

