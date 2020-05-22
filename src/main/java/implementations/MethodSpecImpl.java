package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodSpecImpl implements MethodSpec {

    private final MethodType  type;        // GET, POST, PUT, PATCH, DELETE
    private final RequestSpec request;    // request spec
    private final ResponseSpec response;   // response spec

    MethodSpecImpl(MethodType type, RequestSpec request, ResponseSpec response) {
        this.type = type;
        this.request = request;
        this.response = response;
    }

    public MethodType getType() {
        return this.type;
    }

    public RequestSpec getRequest() {
        return this.request;
    }

    public ResponseSpec getResponse() {
        return this.response;
    }


}