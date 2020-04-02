package implementations;

import java.util.Set;

import interfaces.Header;
import interfaces.ResponseSpec;
import interfaces.Status;

public class ResponseSpecImpl implements ResponseSpec {

    Set<Header> headers;
    Set<Status> statuses;

    public ResponseSpecImpl(Set<Header> headers, Set<Status> statuses) {
        this.headers = headers;
        this.statuses = statuses;
    }

    public Set<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<Header> headers) {
        this.headers = headers;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

}