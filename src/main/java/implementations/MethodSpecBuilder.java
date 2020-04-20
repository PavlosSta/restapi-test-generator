package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodSpecBuilder {

    private String type;        // GET, POST, PUT, PATCH, DELETE
    private RequestSpec req;    // request spec
    private ResponseSpec res;   // response spec

    public MethodSpecBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public MethodSpecBuilder setRequest(RequestSpec req) {
        this.req = req;
        return this;
    }

    public MethodSpecBuilder setResponse(ResponseSpec res) {
        this.res = res;
        return this;
    }

    public MethodSpec build() {
        return new MethodSpecImpl(type, req, res);
    }

}