package implementations;

import interfaces.Method;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodImpl implements Method {

    String type;        // GET, POST, PUT, PATCH, DELETE
    RequestSpec req;    // request spec
    ResponseSpec res;   // response spec

    MethodImpl(String type, RequestSpec req, ResponseSpec res) {
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