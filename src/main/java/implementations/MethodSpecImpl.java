package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodSpecImpl implements MethodSpec {

    private final String type;        // GET, POST, PUT, PATCH, DELETE
    private final RequestSpec request;    // request spec
    private final ResponseSpec response;   // response spec

    MethodSpecImpl(String type, RequestSpec request, ResponseSpec response) {
        this.type = type;
        this.request = request;
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public RequestSpec getRequest() {
        return request;
    }

    public ResponseSpec getResponse() {
        return response;
    }


}