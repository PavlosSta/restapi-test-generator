package implementations;

import interfaces.Method;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

public class MethodImpl implements Method {

    String type;        // GET, POST, PUT, PATCH, DELETE
    RequestSpec req;    // request spec
    ResponseSpec res;   // response spec

    public MethodImpl(String type, RequestSpec req, ResponseSpec res) {
        this.type = type;
        this.req = req;
        this.res = res;
    }

    public String getType() {
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RequestSpec getReq() {
        return req;
    }

    public void setReq(RequestSpec req) {
        this.req = req;
    }

    public ResponseSpec getRes() {
        return res;
    }

    public void setRes(ResponseSpec res) {
        this.res = res;
    }

}