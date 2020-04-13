package interfaces;

import java.util.Set;

public interface RequestSpec {

    Set<HeaderSpec> getHeaders();
    Set<QueryParamSpec>  getQueryParams();
    String getJwt();

}

