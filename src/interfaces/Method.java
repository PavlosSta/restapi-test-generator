package interfaces;

public interface Method {

    String getType();    // GET, POST, PUT, PATCH, DELETE

    RequestSpec getReq();    // request spec
    ResponseSpec getRes();   // response spec

}

