package interfaces;

public interface Method {

    String getType();    // GET, POST, PUT, PATCH, DELETE

    void setType(String type);

    RequestSpec getReq();    // request spec

    void setReq(RequestSpec req);

    ResponseSpec getRes();   // response spec

    void setRes(ResponseSpec ses);

}

