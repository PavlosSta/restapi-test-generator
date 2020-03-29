package interfaces;

import java.util.Set;

public interface RequestSpec {

    Set<Header> getHeaders();
    Set<QueryParam>  getQueryParams();

    String getJwt();

}

