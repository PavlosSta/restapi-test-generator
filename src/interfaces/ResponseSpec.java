package interfaces;

import java.util.Set;

public interface ResponseSpec {

    Set<Header> getHeaders();

    void setHeaders(Set<Header> headers);

    Set<Status> getStatuses();

    void setStatuses(Set<Status> statuses);

}

