package interfaces;

import java.util.Set;

public interface ResponseSpec {

    Set<Header> getHeaders();

    Set<Status> getStatuses();

}

