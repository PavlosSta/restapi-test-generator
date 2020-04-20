package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodSpecImpl implements MethodSpec {

    private final String type;        // GET, POST, PUT, PATCH, DELETE
    private final RequestSpec req;    // request spec
    private final ResponseSpec res;   // response spec

    MethodSpecImpl(String type, RequestSpec req, ResponseSpec res) {
        this.type = type;
        this.req = req;
        this.res = res;
    }

    public String getType() {
        return null;
    }

    public RequestSpec getReq() {
        return req;
    }

    public ResponseSpec getRes() {
        return res;
    }


}