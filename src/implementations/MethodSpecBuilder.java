package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodSpecBuilder {

    private String type;        // GET, POST, PUT, PATCH, DELETE
    private RequestSpec req;    // request spec
    private ResponseSpec res;   // response spec

    public MethodSpecBuilder type(String type) {
        this.type = type;
        return this;
    }

    public MethodSpecBuilder req(RequestSpec req) {
        this.req = req;
        return this;
    }

    public MethodSpecBuilder res(ResponseSpec res) {
        this.res = res;
        return this;
    }

    public MethodSpec build() {
        return new MethodSpecImpl(type, req, res);
    }

}
