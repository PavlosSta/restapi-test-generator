package implementations;

import interfaces.MethodSpec;
import interfaces.RequestSpec;
import interfaces.ResponseSpec;

import java.util.List;

public class MethodSpecImpl implements MethodSpec {

    private final String type;        // GET, POST, PUT, PATCH, DELETE
    private final RequestSpec req;    // request spec
    private final ResponseSpec res;   // response spec

    MethodSpecImpl(String type, List<RequestSpec> ReqList, List<ResponseSpec> ResList) {
        this.type = type;

        this.req = ReqList.get(0);

        this.res = ResList.get(0);
    }

    public String getType() {
        return type;
    }

    public RequestSpec getReq() {
        return req;
    }

    public ResponseSpec getRes() {
        return res;
    }


}